# Tìm hiểu AndroidManifest.xml

> Tuần 2 – Android. Nội dung: vai trò và cấu trúc của file `AndroidManifest.xml`, liên hệ với bài thực hành 4 component (bài 1).

## 1. AndroidManifest.xml là gì?

`AndroidManifest.xml` là file cấu hình **bắt buộc** của mọi ứng dụng Android, nằm ở `app/src/main/AndroidManifest.xml` (tên file cố định). File này cho **hệ điều hành Android, công cụ build (Gradle) và Google Play** biết thông tin về ứng dụng *trước khi* chạy bất kỳ dòng code nào.

| Vai trò | Ý nghĩa |
|---|---|
| Khai báo các thành phần (component) | Activity, Service, BroadcastReceiver, ContentProvider phải được khai báo thì hệ thống mới biết và cho phép sử dụng |
| Khai báo quyền (permission) | Quyền mà app cần dùng (Internet, Camera...) và quyền mà app khác cần có để truy cập component của app |
| Khai báo yêu cầu phần cứng | Ví dụ app cần camera: thẻ `<uses-feature>` |
| Thiết lập thông tin chung | Icon, tên hiển thị, theme của ứng dụng |
| Chọn màn hình khởi động | Activity có `intent-filter` gồm `MAIN` + `LAUNCHER` |

Lưu ý: các thông tin `applicationId`, `minSdk`, `targetSdk`, `versionCode`, `versionName` hiện nay được đặt trong `build.gradle.kts`. Khi build, Gradle ghép chúng vào Manifest.

## 2. Cú pháp XML cơ bản

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application android:label="@string/app_name">
        <activity
            android:name=".MainActivity"
            android:exported="true" />
    </application>

</manifest>
```

- Dòng đầu `<?xml ... ?>`: khai báo phiên bản XML và bảng mã.
- XML là **cây phân cấp**: mỗi file chỉ có **một phần tử gốc** (ở đây là `<manifest>`), bên trong lồng các phần tử con.
- Phần tử có thể viết dạng `<thẻ> ... </thẻ>` hoặc tự đóng `<thẻ ... />` khi không có phần tử con.
- Thuộc tính viết dạng `tên="giá trị"`. Thuộc tính của Android có tiền tố `android:` nhờ khai báo namespace `xmlns:android="..."` ở thẻ gốc.
- `xmlns:tools` là namespace dùng cho công cụ (IDE, build), ví dụ `tools:ignore`, không đi vào app khi chạy.
- `@string/app_name`, `@mipmap/ic_launcher`, `@xml/file_paths`: tham chiếu tới tài nguyên trong thư mục `res/`.
- `.MainActivity`: dấu `.` ở đầu nghĩa là ghép với namespace của app (ví dụ `com.example.ghi_chu.MainActivity`).

## 3. Cấu trúc tổng quát

```
manifest
├── uses-permission        (quyền app cần)
├── uses-feature           (yêu cầu phần cứng)
├── queries                (khai báo app khác cần nhìn thấy, Android 11+)
└── application
    ├── activity
    │   └── intent-filter
    │       ├── action
    │       ├── category
    │       └── data
    ├── service
    ├── receiver
    │   └── intent-filter
    ├── provider
    │   └── meta-data
    └── meta-data
```

## 4. Các thẻ quan trọng

### 4.1 `<manifest>` (thẻ gốc)
Chứa toàn bộ nội dung. Phải có `xmlns:android`. Trước đây có thuộc tính `package`, nay đã chuyển sang `namespace` / `applicationId` trong Gradle.

### 4.2 `<uses-permission>`
Xin quyền sử dụng tính năng được bảo vệ.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

- Quyền thường (normal, ví dụ INTERNET): hệ thống tự cấp khi cài.
- Quyền nguy hiểm (dangerous, ví dụ CAMERA, ĐỊA ĐIỂM): ngoài việc khai báo, app phải **xin người dùng lúc chạy** (runtime permission).

### 4.3 `<uses-feature>`
Khai báo phần cứng/tính năng app cần, Google Play dùng để lọc thiết bị.

```xml
<uses-feature android:name="android.hardware.camera" android:required="false" />
```

### 4.4 `<application>`
Thẻ chứa mọi component của app, đồng thời đặt thuộc tính chung.

| Thuộc tính | Ý nghĩa |
|---|---|
| `android:icon`, `android:roundIcon` | Icon ứng dụng |
| `android:label` | Tên hiển thị |
| `android:theme` | Theme mặc định |
| `android:allowBackup` | Cho phép sao lưu dữ liệu app |
| `android:dataExtractionRules`, `android:fullBackupContent` | Quy tắc sao lưu / chuyển dữ liệu (file trong `res/xml/`) |
| `android:supportsRtl` | Hỗ trợ ngôn ngữ viết từ phải sang trái |
| `android:name` | (tuỳ chọn) class `Application` tuỳ chỉnh |

### 4.5 `<activity>` – một màn hình
| Thuộc tính | Ý nghĩa |
|---|---|
| `android:name` | Tên class Activity |
| `android:exported` | `true`: app khác được mở; `false`: chỉ app mình |
| `android:label`, `android:theme` | Tiêu đề, theme riêng của màn hình |
| `android:screenOrientation` | Cố định hướng màn hình |
| `android:configChanges` | Tự xử lý thay đổi cấu hình (xoay màn hình...) thay vì để hệ thống tạo lại Activity |
| `android:launchMode` | Cách tạo instance của Activity |

Activity khởi động của app được đánh dấu bằng `intent-filter`:

```xml
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
```

### 4.6 `<intent-filter>`
"Bảng tuyên bố" component có thể xử lý loại Intent nào, gồm:
- `<action>`: hành động (MAIN, VIEW, SEND...).
- `<category>`: nhóm (LAUNCHER, DEFAULT...).
- `<data>`: kiểu dữ liệu / URI scheme (ví dụ `mimeType="text/plain"`).

### 4.7 `<service>`
Khai báo Service (xử lý nền, không giao diện).

```xml
<service android:name=".NoteService" android:exported="false" />
```

### 4.8 `<receiver>`
Khai báo BroadcastReceiver **tĩnh** (đăng ký trong Manifest). Receiver cũng có thể đăng ký **động** bằng code (`registerReceiver`) mà không cần khai báo ở Manifest.

| | Đăng ký tĩnh (Manifest) | Đăng ký động (code) |
|---|---|---|
| Nhận sự kiện khi app không chạy | Có thể (với một số broadcast) | Không, chỉ khi còn đang đăng ký |
| Hạn chế | Từ Android 8, nhiều broadcast ngầm không còn nhận được qua Manifest | Phải tự đăng ký/hủy đúng vòng đời |

### 4.9 `<provider>`
Khai báo ContentProvider, ví dụ `FileProvider` để chia sẻ file an toàn.

| Thuộc tính | Ý nghĩa |
|---|---|
| `android:name` | Class provider |
| `android:authorities` | Chuỗi định danh duy nhất của provider (dùng trong URI `content://...`) |
| `android:exported` | Cho app khác truy cập trực tiếp hay không |
| `android:grantUriPermissions` | Cho phép cấp quyền tạm thời trên từng URI |

### 4.10 `<meta-data>`
Cặp tên – giá trị bổ sung cho component/ứng dụng. Với FileProvider, meta-data trỏ tới file `res/xml/file_paths.xml` chứa các thư mục được phép chia sẻ.

### 4.11 `<queries>`
Từ Android 11, app mặc định không "nhìn thấy" các app khác. Muốn truy vấn app khác thì khai báo trong `<queries>`.

## 5. `android:exported`

Từ Android 12 (API 31), nếu `targetSdk` từ 31 trở lên thì mọi `activity`, `service`, `receiver` có `intent-filter` **bắt buộc** khai báo rõ `android:exported`, nếu không build sẽ lỗi `Manifest merger failed`.

- `true`: component có thể được app khác gọi tới.
- `false`: chỉ app mình (hoặc cùng UID) được dùng. Nên đặt `false` nếu không có nhu cầu chia sẻ.

## 6. Ghép Manifest và placeholder

- Các thư viện (AndroidX...) cũng có Manifest riêng. Khi build, Gradle **ghép (merge)** tất cả thành một Manifest cuối cùng. Xem kết quả ghép trong Android Studio: mở `AndroidManifest.xml` → tab **Merged Manifest** ở dưới cùng.
- **Placeholder** dạng `${tên}` được Gradle thay giá trị khi build. Ví dụ `${applicationId}` được thay bằng `applicationId` trong `build.gradle.kts`, nên `${applicationId}.fileprovider` thành `com.example.ghi_chu.fileprovider`.

## 7. Manifest của ứng dụng "Ghi chú học tập" (bài 1)

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Ghi_chu">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.Ghi_chu">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Bài 1.2: Service đếm số từ -->
        <service
            android:name=".NoteService"
            android:exported="false" />

        <!-- Bài 1.4: FileProvider chia sẻ tệp ghi chú -->
        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>
    </application>

</manifest>
```

(Tên theme và một số thuộc tính có thể khác nhẹ tuỳ phiên bản Android Studio.)

Mối liên hệ giữa Manifest và code:

| Trong Manifest | Trong code | Ghi chú |
|---|---|---|
| `<service android:name=".NoteService">` | `class NoteService : Service()`, `Intent(this, NoteService::class.java)` | Thiếu khai báo thì `bindService()` trả `false` |
| `android:authorities="${applicationId}.fileprovider"` | `FileProvider.getUriForFile(this, "$packageName.fileprovider", file)` | Hai chuỗi phải trùng nhau |
| `@xml/file_paths` | file `res/xml/file_paths.xml` | Quy định thư mục được chia sẻ (`cache/shared/`) |
| Không có thẻ `<receiver>` | `ContextCompat.registerReceiver(...)` | Receiver ở bài 1.3 đăng ký động nên không cần khai báo |

## 8. Lỗi thường gặp liên quan đến Manifest

| Lỗi | Nguyên nhân thường gặp |
|---|---|
| `ActivityNotFoundException` | Activity chưa khai báo hoặc sai tên trong Manifest |
| `bindService()` trả `false`, Service không kết nối | Thiếu hoặc sai `<service>`, hoặc đặt ngoài thẻ `<application>` |
| `SecurityException: Permission Denial` | Thiếu `<uses-permission>` hoặc chưa xin quyền lúc chạy |
| `Manifest merger failed ... android:exported` | Component có `intent-filter` nhưng chưa khai báo `android:exported` |
| `Couldn't find meta-data for provider with authority ...` | Thiếu `<provider>` hoặc `authorities` lệch với code |
| `Missing android.support.FILE_PROVIDER_PATHS meta-data` | Thiếu hoặc sai `<meta-data>` của FileProvider |

## 9. Kết luận

`AndroidManifest.xml` là "bản khai báo" của ứng dụng: mọi component phải được khai báo ở đây thì hệ thống mới nhận biết và cho phép sử dụng. File có cấu trúc cây XML với thẻ gốc `<manifest>`, trong đó `<application>` chứa các component, kèm các khai báo quyền và tính năng. Hiểu cấu trúc này giúp xác định nhanh lỗi khi component không chạy hoặc ứng dụng crash do khai báo thiếu/sai.
