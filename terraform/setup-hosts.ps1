# setup-hosts.ps1
# Run this script in PowerShell as Administrator

$hostsPath = "$env:SystemRoot\System32\drivers\etc\hosts"

$entries = @(
    "127.0.0.1 devops-project.test",
    "127.0.0.1 grafana.devops-project.test",
    "127.0.0.1 prometheus.devops-project.test",
    "127.0.0.1 kibana.devops-project.test"
)

# Load existing content
$hostsContent = Get-Content -Path $hostsPath -ErrorAction Stop

# Collect new entries that are missing
$newEntries = @()
foreach ($entry in $entries) {
    if ($hostsContent -notcontains $entry) {
        $newEntries += $entry
        Write-Host "Will add: $entry"
    } else {
        Write-Host "Already exists: $entry"
    }
}

# Append all at once (if needed)
if ($newEntries.Count -gt 0) {
    Add-Content -Path $hostsPath -Value $newEntries
    Write-Host "✅ Added $($newEntries.Count) new entries."
} else {
    Write-Host "✅ Nothing to add."
}

Write-Host "Now run: ipconfig /flushdns"
