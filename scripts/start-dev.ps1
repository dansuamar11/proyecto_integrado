$ErrorActionPreference = 'Stop'

$workspaceRoot = Split-Path -Parent $PSScriptRoot
$frontendRoot = Join-Path $workspaceRoot 'frontend\basketAljarafe'
$logsRoot = Join-Path $workspaceRoot 'logs'
$stateFile = Join-Path $logsRoot 'dev-processes.json'

# Bloque que sirve para crear la carpeta de logs del entorno de desarrollo.
New-Item -ItemType Directory -Force -Path $logsRoot | Out-Null

if (-not (Test-Path (Join-Path $frontendRoot 'node_modules'))) {
  throw "No existe la carpeta node_modules del frontend. Ejecuta npm install dentro de frontend\basketAljarafe."
}

function Test-PortInUse {
  param (
    [Parameter(Mandatory = $true)]
    [int]$Port
  )

  return [bool](Get-NetTCPConnection -LocalPort $Port -ErrorAction SilentlyContinue)
}

function Test-ProcessAlive {
  param (
    [Parameter(Mandatory = $true)]
    [int]$ProcessId
  )

  return [bool](Get-Process -Id $ProcessId -ErrorAction SilentlyContinue)
}

function Stop-ProcessIfRunning {
  param (
    [Parameter(Mandatory = $true)]
    [int]$ProcessId
  )

  $process = Get-Process -Id $ProcessId -ErrorAction SilentlyContinue

  if ($process) {
    Stop-Process -Id $ProcessId -Force
  }
}

function Get-AngularListeningProcessIds {
  param (
    [Parameter(Mandatory = $true)]
    [int[]]$Ports
  )

  $processIds = @()

  foreach ($port in $Ports) {
    $connections = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue

    foreach ($connection in $connections) {
      $process = Get-CimInstance Win32_Process -Filter "ProcessId = $($connection.OwningProcess)" -ErrorAction SilentlyContinue

      if ($process -and $process.CommandLine -like '*BASKET_ALJARAFE*ng.js*serve*') {
        $processIds += [int]$connection.OwningProcess
      }
    }
  }

  return $processIds | Select-Object -Unique
}

function Get-FrontendPort {
  $candidatePorts = @(4200, 4201, 4202, 4203, 4204, 4205)

  foreach ($candidatePort in $candidatePorts) {
    if (-not (Test-PortInUse -Port $candidatePort)) {
      return $candidatePort
    }
  }

  throw 'No hay puertos disponibles entre 4200 y 4205 para iniciar Angular.'
}

function Get-ListeningProcessId {
  param (
    [Parameter(Mandatory = $true)]
    [int]$Port
  )

  $connection = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1

  if ($connection) {
    return [int]$connection.OwningProcess
  }

  return $null
}

function Start-BackgroundCommand {
  param (
    [Parameter(Mandatory = $true)]
    [string]$Command,

    [Parameter(Mandatory = $true)]
    [string]$WorkingDirectory,

    [Parameter(Mandatory = $true)]
    [string]$LogPath
  )

  $wrappedCommand = "$Command > `"$LogPath`" 2>&1"

  return Start-Process -FilePath 'cmd.exe' `
    -ArgumentList @('/c', $wrappedCommand) `
    -WorkingDirectory $WorkingDirectory `
    -PassThru `
    -WindowStyle Minimized
}

function Wait-BackendReady {
  param (
    [Parameter(Mandatory = $true)]
    [int]$Port,

    [Parameter(Mandatory = $true)]
    [int]$TimeoutSeconds
  )

  $deadline = (Get-Date).AddSeconds($TimeoutSeconds)

  while ((Get-Date) -lt $deadline) {
    try {
      $response = Invoke-WebRequest -UseBasicParsing -Uri "http://127.0.0.1:$Port/api/publico/inicio" -TimeoutSec 2

      if ($response.StatusCode -ge 200 -and $response.StatusCode -lt 300) {
        return $true
      }
    }
    catch {
      Start-Sleep -Milliseconds 500
    }
  }

  return $false
}

if (Test-Path $stateFile) {
  $previousState = Get-Content $stateFile | ConvertFrom-Json
  $backendAlive = Test-ProcessAlive -ProcessId ([int]$previousState.backendPid)
  $frontendAlive = Test-ProcessAlive -ProcessId ([int]$previousState.frontendPid)

  if ($backendAlive -or $frontendAlive) {
    Write-Host 'El entorno de desarrollo ya esta iniciado.' -ForegroundColor Yellow
    Write-Host "Frontend: $($previousState.frontendUrl)"
    Write-Host 'Backend: http://localhost:1000/'
    Write-Host "Log backend: $($previousState.backendLog)"
    Write-Host "Log frontend: $($previousState.frontendLog)"
    Write-Host 'Si quieres reiniciar los procesos, ejecuta npm run dev:stop y luego npm run dev:full.'
    exit 0
  }

  Remove-Item -LiteralPath $stateFile -Force
}

# Bloque que sirve para detener servidores Angular antiguos de este proyecto que sigan ocupando puertos de desarrollo.
$staleAngularProcessIds = Get-AngularListeningProcessIds -Ports @(4200, 4201, 4202, 4203, 4204, 4205)

foreach ($staleAngularProcessId in $staleAngularProcessIds) {
  Stop-ProcessIfRunning -ProcessId $staleAngularProcessId
}

if ($staleAngularProcessIds.Count -gt 0) {
  Start-Sleep -Seconds 2
}

$frontendPort = Get-FrontendPort
$timestamp = Get-Date -Format 'yyyyMMdd-HHmmss'
$backendLog = Join-Path $logsRoot "backend-dev-$timestamp.log"
$frontendLog = Join-Path $logsRoot "frontend-dev-$timestamp.log"

# Bloque que sirve para reutilizar el backend activo o iniciarlo en segundo plano si no existe.
$backendListeningProcessId = Get-ListeningProcessId -Port 1000

if ($backendListeningProcessId) {
  $backendPid = $backendListeningProcessId
  $backendLog = 'Backend ya iniciado previamente en el puerto 1000.'
}
else {
  $backendProcess = Start-BackgroundCommand `
    -Command 'mvnw.cmd spring-boot:run' `
    -WorkingDirectory $workspaceRoot `
    -LogPath $backendLog

  $backendPid = $backendProcess.Id

  if (-not (Wait-BackendReady -Port 1000 -TimeoutSeconds 45)) {
    throw 'El backend no ha quedado listo a tiempo en el puerto 1000.'
  }
}

# Bloque que sirve para iniciar Angular en segundo plano sobre el puerto disponible.
$frontendProcess = Start-BackgroundCommand `
  -Command "npm.cmd start -- --host 0.0.0.0 --port $frontendPort" `
  -WorkingDirectory $frontendRoot `
  -LogPath $frontendLog

$state = [PSCustomObject]@{
  backendPid = $backendPid
  frontendPid = $frontendProcess.Id
  backendPort = 1000
  frontendPort = $frontendPort
  frontendUrl = "http://127.0.0.1:$frontendPort/"
  backendLog = $backendLog
  frontendLog = $frontendLog
  startedAt = (Get-Date).ToString('s')
}

# Bloque que sirve para guardar el estado de los procesos iniciados.
$state | ConvertTo-Json | Set-Content -Path $stateFile -Encoding UTF8

Write-Host ''
Write-Host 'Entorno de desarrollo iniciado.' -ForegroundColor Green
Write-Host "Frontend: $($state.frontendUrl)"
Write-Host 'Backend: http://127.0.0.1:1000/'
Write-Host "Log backend: $backendLog"
Write-Host "Log frontend: $frontendLog"
Write-Host ''
Write-Host 'Si quieres detener ambos procesos, ejecuta npm run dev:stop desde la raiz del proyecto.'
