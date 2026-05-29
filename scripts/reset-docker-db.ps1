$ErrorActionPreference = 'Stop'

$projectRoot = Split-Path -Parent $PSScriptRoot
$dataRoot = Join-Path $projectRoot 'docker\mysql\data'

Push-Location $projectRoot
try {
    docker compose down

    if (Test-Path $dataRoot) {
        Get-ChildItem -Force $dataRoot |
            Where-Object { $_.Name -ne '.gitkeep' } |
            Remove-Item -Recurse -Force
    }

    docker compose up --build -d

    Write-Host 'Base Docker reinicializada con la semilla actual.'
}
finally {
    Pop-Location
}
