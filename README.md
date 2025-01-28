# PDF Reader Application

This is a simple Android application that displays PDF files stored on your device. It features a RecyclerView to list PDFs, a PDF viewing activity, and the ability to star PDFs to prioritize them. Starred PDFs are saved and displayed at the top of the list, even after restarting the application.

## Features

- **PDF Listing:** Scans the device storage and displays all PDF files.
- **Starred PDFs:** Users can star PDFs, and these will be shown at the top of the list.
- **PDF Viewer:** Opens a selected PDF in a dedicated viewer activity.
- **Runtime Permissions:** Handles runtime permissions for accessing device storage.
- **Proper Shutdown:** Implements an `onBackPressed` method for app shutdown.

---

<img src="[https://example.com/image.png](https://github.com/user-attachments/assets/9b43c001-d36f-4269-9af2-dd14f61d32a5)" alt="Example Image" width="300">

## Permissions

### Required Permissions

- `READ_EXTERNAL_STORAGE`: For accessing PDF files on the device.
- `MANAGE_EXTERNAL_STORAGE`: For devices running Android 11+ to access all files.

### Requesting Permissions

The app uses the Dexter library to handle permissions dynamically. If the required permissions are not granted, the app will request them at runtime.

---

## Implementation Details

### Main Features

1. **PDF Listing**
   - Uses a recursive function to scan device storage for PDFs.
   - Displays PDFs in a grid layout using `RecyclerView`.

2. **Starred PDFs**
   - Starred status is saved in `SharedPreferences`.
   - On reopening the app, starred PDFs are loaded first.

3. **PDF Viewer**
   - Uses `com.github.barteksc:android-pdf-viewer` library to render PDFs.

4. **Proper Shutdown**
   - `onBackPressed` method ensures the app closes properly.

---

## Libraries Used

- **Dexter**: Simplifies runtime permission handling.
- **Android PDF Viewer**: Library for viewing PDF files.

---
