@echo off
del .\bin\jukagaka\* /s /q

call comp-single .\src\jukagaka\UkaComponent.java
call comp-single .\src\jukagaka\UkaDaemon.java
call comp-single .\src\jukagaka\UkaTray.java

call comp-single .\src\jukagaka\shell\UkaWindow.java

call comp-single .\src\jukagaka\shell\renderer\UkagakaRenderer.java
call comp-single .\src\jukagaka\shell\renderer\BalloonRenderer.java

call comp-single .\src\jukagaka\shell\Ukagaka.java
call comp-single .\src\jukagaka\shell\Balloon.java

call comp-single .\src\jukagaka\shell\renderer\FakeComponent.java
call comp-single .\src\jukagaka\shell\renderer\SwingRenderer.java

call comp-single .\src\jukagaka\UkaShell.java
call comp-single .\src\jukagaka\UkaGhost.java
call comp-single .\src\jukagaka\UkaPlugin.java
