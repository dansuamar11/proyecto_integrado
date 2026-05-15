$ErrorActionPreference = 'Stop'

$workspaceRoot = Split-Path -Parent $PSScriptRoot
$logsRoot = Join-Path $workspaceRoot 'logs'
$stateFile = Join-Path $logsRoot 'dev-processes.json'

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
  $candidatePorts = @(4200, 4201, 4202, 4203, 4204, 4205)
  $processIds = @()

  foreach ($candidatePort in $candidatePorts) {
    $connections = Get-NetTCPConnection -LocalPort $candidatePort -State Listen -ErrorAction SilentlyContinue

    foreach ($connection in $connections) {
      $process = Get-CimInstance Win32_Process -Filter "ProcessId = $($connection.OwningProcess)" -ErrorAction SilentlyContinue

      if ($process -and $process.CommandLine -like '*BASKET_ALJARAFE*ng.js*serve*') {
        $processIds += [int]$connection.OwningProcess
      }
    }
  }

  return $processIds | Select-Object -Unique
}

if (Test-Path $stateFile) {
  $state = Get-Content $stateFile | ConvertFrom-Json

  # Bloque que sirve para detener el proceso del backend si sigue activo.
  Stop-ProcessIfRunning -ProcessId ([int]$state.backendPid)

  # Bloque que sirve para detener el proceso del frontend si sigue activo.
  Stop-ProcessIfRunning -ProcessId ([int]$state.frontendPid)
}
else {
  Write-Host 'No existe un estado guardado de procesos de desarrollo. Se intentara limpiar Angular igualmente.' -ForegroundColor Yellow
}

# Bloque que sirve para detener servidores Angular antiguos de este proyecto que sigan activos.
$staleAngularProcessIds = Get-AngularListeningProcessIds

foreach ($staleAngularProcessId in $staleAngularProcessIds) {
  Stop-ProcessIfRunning -ProcessId $staleAngularProcessId
}

if (Test-Path $stateFile) {
  Remove-Item -LiteralPath $stateFile -Force
}

Write-Host 'Procesos de desarrollo detenidos.' -ForegroundColor Green
