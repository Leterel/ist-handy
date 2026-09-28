param(
    [Parameter(Mandatory=$true)][ValidatePattern('^emulator-[0-9]+$')][string]$Serial,
    [Parameter(Mandatory=$true)][string]$AdbPath,
    [Parameter(Mandatory=$true)][string]$ApkPath,
    [Parameter(Mandatory=$true)][string]$OutputDirectory
)
$ErrorActionPreference='Stop'
$AdbPath=(Resolve-Path -LiteralPath $AdbPath).Path
$ApkPath=(Resolve-Path -LiteralPath $ApkPath).Path
New-Item -ItemType Directory -Force -Path $OutputDirectory | Out-Null
$OutputDirectory=(Resolve-Path -LiteralPath $OutputDirectory).Path
$script:checks=0
function Adb([Parameter(ValueFromRemainingArguments=$true)][string[]]$Arguments) {
    $qemu=& $AdbPath -s $Serial shell getprop ro.kernel.qemu
    if ($LASTEXITCODE -ne 0 -or "$qemu".Trim() -ne '1') { throw "Refusing action: $Serial is not a verified emulator" }
    $result=& $AdbPath -s $Serial @Arguments
    if ($LASTEXITCODE -ne 0) { throw "Emulator command failed: $Arguments" }
    return $result
}
function Check([bool]$Condition,[string]$Message) {
    if (!$Condition) { throw "FAIL: $Message" }
    $script:checks++; Write-Output "PASS: $Message"
}
$oldCar=(Adb @('shell','cmd','uimode','car')).Trim()
if($oldCar -notmatch '^Car mode: (yes|no)$'){throw 'Cannot determine original car mode'}
$oldCarValue=$Matches[1]
$oldSize=Adb @('shell','wm','size')
$oldDensity=Adb @('shell','wm','density')
$oldFont=(Adb @('shell','settings','get','system','font_scale')).Trim()
$features=Adb @('shell','pm','list','features')
$features | Set-Content (Join-Path $OutputDirectory 'emulator-features.txt')
Check (!($features -contains 'feature:android.hardware.type.automotive')) 'Emulator has no automotive hardware feature'
try {
    Adb @('install','-r',$ApkPath) | Out-Null
    Adb @('shell','settings','put','system','font_scale','1.0') | Out-Null
    foreach($layout in @(
        @{Name='phone-car';Size='1080x1920';Density='420';Title='Ja, is Handy, Mois.'},
        @{Name='tablet-car';Size='1600x2560';Density='320';Title='Nee, is Tablet.'}
    )) {
        Adb @('shell','wm','size',$layout.Size) | Out-Null
        Adb @('shell','wm','density',$layout.Density) | Out-Null
        Adb @('shell','cmd','uimode','car','yes') | Out-Null
        Adb @('shell','am','force-stop','de.leterel.isthandy') | Out-Null
        Adb @('shell','am','start','-W','-n','de.leterel.isthandy/.MainActivity') | Out-Null
        Start-Sleep -Seconds 8
        $service=(Adb @('shell','dumpsys','uimode')) -join "`n"
        $service | Set-Content (Join-Path $OutputDirectory ($layout.Name+'-uimode.txt'))
        if($service -notmatch 'mCurUiMode=0x([0-9a-fA-F]+)'){throw 'Missing UI mode in service dump'}
        $uiMode=[Convert]::ToInt32($Matches[1],16)
        Check (($uiMode -band 0x0f) -eq 3) ($layout.Name+' actually uses Android car UI mode')
        Adb @('shell','rm','-f','/sdcard/codex-car-mode.xml') | Out-Null
        Adb @('shell','uiautomator','dump','/sdcard/codex-car-mode.xml') | Out-Null
        $xmlPath=Join-Path $OutputDirectory ($layout.Name+'.xml')
        Adb @('pull','/sdcard/codex-car-mode.xml',$xmlPath) | Out-Null
        [xml]$xml=Get-Content -LiteralPath $xmlPath -Raw
        Check (@($xml.SelectNodes('//node') | Where-Object {$_.text -eq $layout.Title}).Count -gt 0) ($layout.Name+' preserves correct device verdict')
        Adb @('shell','screencap','-p','/sdcard/codex-car-mode.png') | Out-Null
        Adb @('pull','/sdcard/codex-car-mode.png',(Join-Path $OutputDirectory ($layout.Name+'.png'))) | Out-Null
    }
    [ordered]@{
        testedAt=(Get-Date).ToString('o');serial=$Serial;api=(Adb @('shell','getprop','ro.build.version.sdk')).Trim();
        apk=(Split-Path $ApkPath -Leaf);sha256=(Get-FileHash -LiteralPath $ApkPath).Hash;
        checks=$script:checks;scope='Real Android car UI mode on one API-34 emulator without automotive hardware; phone and tablet display configurations'
    } | ConvertTo-Json | Set-Content (Join-Path $OutputDirectory 'car-mode-results.json')
} finally {
    Adb @('shell','cmd','uimode','car',$oldCarValue) | Out-Null
    $size=if("$oldSize" -match 'Override size: (\d+x\d+)'){$Matches[1]}else{'reset'}
    $density=if("$oldDensity" -match 'Override density: (\d+)'){$Matches[1]}else{'reset'}
    Adb @('shell','wm','size',$size) | Out-Null
    Adb @('shell','wm','density',$density) | Out-Null
    if($oldFont -eq 'null'){Adb @('shell','settings','delete','system','font_scale') | Out-Null}
    else {Adb @('shell','settings','put','system','font_scale',$oldFont) | Out-Null}
    if((Adb @('shell','cmd','uimode','car')).Trim() -ne $oldCar){throw 'Car mode was not restored'}
}
