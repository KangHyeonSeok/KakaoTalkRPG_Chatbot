cd AndroidProject
call gradlew NotiListener:build
cd ..
copy /Y AndroidProject\NotiListener\build\outputs\aar\NotiListener-debug.aar UnityProject\Assets\Plugins\Android\NotiListener-debug.aar

pause