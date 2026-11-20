# 🌦️ Transparent Weather Widget (Android)

Ứng dụng & Widget thời tiết màn hình chính nền trong suốt dành cho Android (Kotlin).

## ✨ Tính năng chính
- **Widget nền trong suốt hoàn toàn**, hòa quyện với hình nền điện thoại.
- **Đồng hồ Flip** ngăn cách giữa giờ và phút bằng dấu chấm (`HH.mm`).
- **Bố cục 3 cột trực quan**:
  - **Bên trái**: Tự động định vị tên **Xã / Phường** hiện tại qua GPS & Geocoder.
  - **Ở giữa**: Biểu tượng thời tiết (chạm để mở trang thời tiết chi tiết kèm hiệu ứng).
  - **Bên phải**: Nhiệt độ thực tế (lấy từ API Open-Meteo miễn phí, không cần API Key).
- **Hỗ trợ điều chỉnh kích thước (Resize)**: Nhấn giữ Widget trên màn hình chính để kéo thay đổi kích thước ngang/dọc.
- **Hiệu ứng thời tiết sống động**: Mưa bay nghiêng theo tốc độ gió và sấm chớp lóe sáng khi mở trang chi tiết.

## 🚀 Cách tải file `.apk` tự động trên GitHub (Không cần cài Android Studio)
1. Tải toàn bộ mã nguồn này lên một Repository mới trên **GitHub**.
2. Chuyển sang tab **Actions** trên Repository của bạn.
3. Chọn workflow **Build Android Debug APK** -> Nhấn **Run workflow** (hoặc chờ hệ thống tự chạy sau khi push code).
4. Sau khi build xong (khoảng 2-3 phút), kéo xuống mục **Artifacts** và tải tệp **`TransparentWeather-Debug-APK`** về cài đặt trên điện thoại Android.
