# Tuvio Home

Лёгкий главный экран для Tuvio TD50UFBSV1 на Android 11. Верхний ряд открывает YouTube, Twitch, TV Bro и VK Видео. Вкладка «Приложения» показывает остальные приложения с интерфейсом запуска, включая установленные вручную. На главном экране «Назад» сохраняет фокус в лаунчере, в библиотеке возвращает на главную.

## Установка версии 1.4

Скачайте [подписанный APK](TuvioHome-v1.4.apk). На компьютере с Android Platform Tools подключитесь по ADB к своему телевизору. Выполняйте команды при одном подключённом устройстве.

    adb install -r TuvioHome-v1.4.apk
    adb shell am start -n local.tuvio.home/.Home
    adb shell cmd package set-home-activity --user 0 local.tuvio.home/.Home
    adb shell input keyevent 3

Проверьте пульт, кнопку Home и запуск плиток. Команды установки сохраняют штатный лаунчер.

## Возврат к Яндексу

    adb shell pm enable --user 0 com.yandex.tv.home
    adb shell cmd package set-home-activity --user 0 com.yandex.tv.home/.HomeActivity
    adb shell input keyevent 3

## Устройство проекта

[Исходник экрана](launcher/Home.java) содержит плитки, обработку пульта и библиотеку. [Манифест](launcher/AndroidManifest.xml) регистрирует приложение как домашний экран. [HTML-макет](home-preview.html) показывает ранний вариант оформления. В [журнале](tv-cleanup-log.md) записаны изменения конкретного телевизора и команды отката.

Для сборки использованы JDK 17, Android SDK Platform 30 и Build Tools 34.0.0. Home.java компилируется javac с --release 8 и android.jar в classpath. D8 преобразует классы в classes.dex. AAPT упаковывает манифест, после добавления classes.dex APK выравнивается zipalign и подписывается apksigner. Обновление установленного приложения требует того же ключа подписи.



Панель продолжения открывает ссылку из Android Watch Next. Если список пуст, нажмите OK и сохраните HTTPS-ссылку. Долгое нажатие OK открывает редактор закладки. Для чтения Watch Next после установки: adb shell pm grant local.tuvio.home android.permission.READ_TV_LISTINGS.
