# Tuvio TD50UFBSV1 — журнал работ

## 2026-11-07 — исходное состояние
- Android 11; сборка RP1A.200720.011; заявленный патч безопасности 2021-03-05.
- Пользователь включил ADB Mode в сервисном меню и подтвердил подключение с Windows.
- Подготовлен Android Platform-Tools 37.0.1.
- Локально сохранены getprop, списки пакетов, dumpsys package, настройки global/secure/system, meminfo и df.
- Это диагностический снимок, а не полный образ прошивки или резервная копия данных приложений.
- Обнаружено 95 пакетов; список отключённых пакетов пуст.
- На /data доступно около 299 МБ.
- Голосовой помощник назначен на службу AliceVoiceInteractionService пакета com.yandex.io.sdk.
- Текущая клавиатура: ru.yandex.androidkeyboard.tv.
- com.android.systemui поставляется из YandexSystemUI.apk.
- Google Play Store и Google Play Services в списке пакетов не обнаружены.

## Изменения приложений и настроек
Пакеты не отключались и не удалялись. Альтернативный лаунчер не установлен. Прошивка не изменялась.

## Следующие шаги
1. Подготовить альтернативный главный экран и проверить управление пультом.
2. Сохранить штатный главный экран до проверки Home и перезагрузки.
3. Проверить зависимости компонентов Алисы перед выборочным отключением.
4. Для каждого изменения записывать исходное состояние, результат проверки и точный откат.

## Конфиденциальность
Сырые выгрузки настроек, идентификаторы устройства, адреса сети и данные авторизации в репозиторий не включать.

## 2026-11-07 — место и Алиса
Сохранены APK 21 приложения из /data/app, проверены SHA-256 на ПК и ТВ. Полный образ прошивки недоступен через обычный ADB. Вручную разрешён переход к изменениям с копиями APK.
Удалены обновления com.yandex.tv.alice и com.yandex.io.sdk. Пакет com.yandex.tv.alice отключён для пользователя 0. Прошивка защищает com.yandex.io.sdk от отключения. Для него выставлен режим RECORD_AUDIO ignore (исходно allow). Home открывает штатную HomeActivity. На /data стало доступно 446 МиБ, до работ было 299 МиБ.
Для возврата включить com.yandex.tv.alice, установить прежние версии обоих пакетов из локальной копии через adb install -r и вернуть режим RECORD_AUDIO allow. APK не содержат приватные данные приложений.
Макет home-preview.html доступен для согласования перед установкой лаунчера.
Проверка AppOps: запрет установлен на уровне UID. Для снятия: adb shell cmd appops set --uid com.yandex.io.sdk RECORD_AUDIO default. Разрешение самого пакета показывает allow.

## 2026-11-07 — сторонние приложения и обновления
Установлены F-Droid и Happ из официальных источников. APK Happ проверен по SHA-256 релиза. F-Droid получил REQUEST_INSTALL_PACKAGES allow. Для возврата настройки: cmd appops set org.fdroid.fdroid REQUEST_INSTALL_PACKAGES default. Новые приложения удаляются через настройки ТВ.
Для UID com.yandex.launcher.updaterapp выставлен POST_NOTIFICATION ignore (возврат: cmd appops set --uid com.yandex.launcher.updaterapp POST_NOTIFICATION default). Установлен глобальный ota_disable_automatic_update=1 (возврат: settings delete global ota_disable_automatic_update). Прошивка отклонила отключение всего пакета, старые уведомления оставались при проверке. Полная блокировка обновлений не подтверждена. После установки приложений на /data свободно 291 МиБ.
Отключён отправщик отчётов com.yandex.tv.bugreportsender. Откат: pm default-state --user 0 com.yandex.tv.bugreportsender. Запуск главного окна Happ проверен через dumpsys window.
Установлен и запущен TV Bro 2.1.6 (WebView, пакет com.phlox.tvwebbrowser), SHA-256 APK совпал с релизом. Блокировка рекламы включена по умолчанию. Откат установки: adb uninstall com.phlox.tvwebbrowser. Свободно до установки 262 МиБ, после 254 МиБ, после pm trim-caches 400M — 408 МиБ. Кэш может накопиться снова.
Откат обновления com.yandex.browser.tv: перед операцией SHA-256 активного APK совпал с архивной копией. После отката используется /system/app/YandexBrowser/YandexBrowser.apk. Возврат версии: adb install -r с сохранённым base.apk этого пакета. Свободно до операции 379 МиБ, после 461 МиБ. TV Bro открыт, активный WebView 144.0.7559.226.
Установлен local.tuvio.home/.Home и выбран по умолчанию. Проверены запуск TV Bro стрелкой и OK и возврат нового экрана по Home. Возврат штатного: adb shell cmd package set-home-activity --user 0 com.yandex.tv.home/.HomeActivity.
Лаунчер v1.1: Back остаётся на главном, из библиотеки возвращается на главный. Установлены плитки F-Droid и Яндекс Музыки, библиотека строится по запускным приложениям. com.yandex.tv.home отключён через pm disable-user --user 0, его процесс отсутствует. Для возврата с ПК сначала pm enable --user 0 com.yandex.tv.home, затем cmd package set-home-activity --user 0 com.yandex.tv.home/.HomeActivity. Выбрана LeanKeyboard 6.1.31, прежний метод ввода выключен. Откат клавиатуры: ime enable и затем ime set для ru.yandex.androidkeyboard.tv/ru.yandex.androidkeyboard.TvYandexKeyboardService.
