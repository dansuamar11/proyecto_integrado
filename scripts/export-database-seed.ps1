param(
    [string]$OutputFile = "docker/mysql/init/02-seed.sql",
    [string]$SourceHost = "host.docker.internal",
    [int]$SourcePort = 3306,
    [string]$Database = "BASKETALJARAFE",
    [string]$Username = "root",
    [string]$Password = "12345"
)

$projectRoot = Split-Path -Parent $PSScriptRoot
$outputPath = Join-Path $projectRoot $OutputFile
$outputDirectory = Split-Path -Parent $outputPath

if (-not (Test-Path $outputDirectory)) {
    New-Item -ItemType Directory -Path $outputDirectory | Out-Null
}

$orderedTables = @(
    'rol',
    'usuario',
    'equipo',
    'jugador',
    'partido',
    'estadisticas_partido',
    'solicitud_contacto'
)

Push-Location $projectRoot
try {
    docker run --rm mysql:8.4 mysqldump `
        -h $SourceHost `
        -P $SourcePort `
        -u $Username `
        "-p$Password" `
        --set-gtid-purged=OFF `
        --no-tablespaces `
        --skip-comments `
        --skip-dump-date `
        --complete-insert `
        --skip-add-drop-table `
        --databases $Database `
        --tables $orderedTables |
        Set-Content -Path $outputPath -Encoding UTF8

    Write-Host "Seed exportado a $outputPath"
}
finally {
    Pop-Location
}
