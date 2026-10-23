# Tổng quan Kiến thức Android Cơ bản

## 1. Các Components chính của Android

Android có **4 thành phần cơ bản** đóng vai trò là các điểm truy cập chính mà hệ điều hành hoặc người dùng có thể đi vào ứng dụng.

### A. Activity
* **Tác dụng:** Đại diện cho một màn hình đơn lẻ với giao diện người dùng. Cho phép người dùng tương tác với ứng dụng (ví dụ: màn hình đăng nhập, màn hình danh sách).
* **Ví dụ Code:**
  ```kotlin
  import android.os.Bundle
  import androidx.appcompat.app.AppCompatActivity

  class MainActivity : AppCompatActivity() {
      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          // Gán giao diện layout XML cho Activity
          setContentView(R.layout.activity_main)
      }
  }
  ```

### B. Service
* **Tác dụng:** Chạy dưới nền để thực hiện các tác vụ kéo dài mà không cần giao diện người dùng. Ví dụ: phát nhạc nền, tải file lớn.
* **Ví dụ Code:**
  ```kotlin
  import android.app.Service
  import android.content.Intent
  import android.os.IBinder

  class MusicService : Service() {
      override fun onBind(intent: Intent?): IBinder? {
          return null // Trả về null nếu đây là Started Service
      }

      override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
          // Thực hiện tác vụ chạy phát nhạc dưới nền ở đây
          return START_STICKY
      }
  }
  ```

### C. Broadcast Receiver
* **Tác dụng:** Lắng nghe và phản hồi lại các thông điệp phát ra từ hệ thống hoặc ứng dụng khác. Ví dụ: thông báo pin yếu, máy bật chế độ máy bay, cắm sạc.
* **Ví dụ Code:**
  ```kotlin
  import android.content.BroadcastReceiver
  import android.content.Context
  import android.content.Intent
  import android.widget.Toast

  class AirplaneModeReceiver : BroadcastReceiver() {
      override fun onReceive(context: Context?, intent: Intent?) {
          if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
              Toast.makeText(context, "Trạng thái chế độ máy bay đã thay đổi!", Toast.LENGTH_SHORT).show()
          }
      }
  }
  ```

### D. Content Provider
* **Tác dụng:** Quản lý và chia sẻ dữ liệu giữa các ứng dụng với nhau một cách an toàn. Ví dụ: ứng dụng xin quyền đọc danh bạ điện thoại của hệ thống.
* **Ví dụ Code (Truy xuất dữ liệu từ ContentProvider):**
  ```kotlin
  import android.provider.ContactsContract
  
  fun getContacts() {
      val cursor = contentResolver.query(
          ContactsContract.Contacts.CONTENT_URI,
          null, null, null, null
      )
      cursor?.use {
          while (it.moveToNext()) {
              val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
              if (nameIndex >= 0) {
                  val name = it.getString(nameIndex)
                  println("Contact Name: $name")
              }
          }
      }
  }
  ```

---

## 2. Khung xương dự án Android (Cấu trúc thư mục Android Studio)

Khi tạo một project trên Android Studio (chế độ xem **Android**), cấu trúc khung xương chính gồm:

* **`manifests/AndroidManifest.xml`**:
  * **Tác dụng:** File cấu hình trung tâm của app. Khai báo các Activity, Service, BroadcastReceiver, ContentProvider, cấp quyền hệ thống (permissions như Camera, Internet), và cấu hình tên/icon của app.
* **`java/` (hoặc `kotlin/`)**:
  * **Tác dụng:** Chứa toàn bộ mã nguồn xử lý logic của dự án (các lớp Kotlin/Java, ViewModel, Repository, v.v.).
* **`res/` (Resources)**:
  * **`layout/`**: Chứa các file giao diện XML (UI layout).
  * **`drawable/`**: Chứa hình ảnh, icon, hình nền đồ họa vector (`.xml`, `.png`, `.jpg`).
  * **`values/`**: Chứa tài nguyên dữ liệu tĩnh: `strings.xml` (chuỗi văn bản), `colors.xml` (mã màu), `themes.xml` (giao diện/theme).
  * **`mipmap/`**: Chứa các icon ứng dụng (launcher icons) tối ưu cho từng độ phân giải màn hình.
* **`Gradle Scripts/`**:
  * **`build.gradle.kts (Project)`**: Cấu hình chung cho toàn bộ dự án (plugin, phiên bản Kotlin,...).
  * **`build.gradle.kts (Module :app)`**: Cấu hình chi tiết cho app: SDK target, SDK minimum, phiên bản ứng dụng và các thư viện phụ thuộc.

---

## 3. Vòng đời của Activity / App Android

Vòng đời của ứng dụng/màn hình Android chuyển đổi qua các trạng thái chính thông qua các phương thức callback:

```
      [ App Launched ]
             │
             ▼
        onCreate()    ──> (Tạo UI, khởi tạo data)
             │
             ▼
        onStart()     ──> (Màn hình bắt đầu hiển thị)
             │
             ▼
        onResume()    ──> (Màn hình active, người dùng tương tác được)
             │
             ▼
        onPause()     ──> (Bị mất focus một phần, ví dụ: hiện pop-up)
             │
             ▼
        onStop()      ──> (Màn hình bị ẩn hoàn toàn)
             │
             ▼
       onDestroy()    ──> (Hủy Activity, giải phóng bộ nhớ)
```

### Chi tiết các Trạng thái và Callback:
1. **`onCreate()`**:
   * **Trạng thái:** *Created*.
   * **Ý nghĩa:** Gọi khi Activity được khởi tạo lần đầu. Nơi để gán layout (`setContentView`), khởi tạo biến và cài đặt UI.
2. **`onStart()`**:
   * **Trạng thái:** *Started*.
   * **Ý nghĩa:** Màn hình chuẩn bị hiển thị cho người dùng thấy nhưng chưa thể tương tác.
3. **`onResume()`**:
   * **Trạng thái:** *Resumed (Running)*.
   * **Ý nghĩa:** Màn hình đã hiển thị hoàn toàn trên cùng và người dùng có thể tương tác trực tiếp. Đây là trạng thái hoạt động chính.
4. **`onPause()`**:
   * **Trạng thái:** *Paused*.
   * **Ý nghĩa:** Màn hình bị che khuất một phần (như xuất hiện hộp thoại/dialog) hoặc khi chuẩn bị chuyển sang Activity khác. Nơi tạm dừng animation hoặc lưu tạm dữ liệu.
5. **`onStop()`**:
   * **Trạng thái:** *Stopped*.
   * **Ý nghĩa:** Màn hình hoàn toàn bị ẩn khỏi tầm mắt người dùng (khi bấm Home hoặc sang ứng dụng khác).
6. **`onRestart()`**:
   * **Ý nghĩa:** Gọi khi ứng dụng được mở lại từ trạng thái *Stopped* trước khi đi vào `onStart()`.
7. **`onDestroy()`**:
   * **Trạng thái:** *Destroyed*.
   * **Ý nghĩa:** Gọi trước khi Activity bị tiêu hủy hoàn toàn (khi bấm Back, gọi `finish()`, hoặc hệ thống thu hồi bộ nhớ).
