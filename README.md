# 🏥 Rahi's Care Hospital - Android

A comprehensive Android application for managing hospital operations, patient records, appointments, and medical services. Designed with role-based access for patients, doctors, and administrators.

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Setup & Installation](#setup--installation)
- [Usage Guide](#usage-guide)
- [Architecture](#architecture)
- [Dependencies](#dependencies)
- [User Roles](#user-roles)

## 📱 Overview
Rahi's Care Hospital is a full-featured Android application that streamlines hospital operations and improves patient care delivery. The system supports multiple user types (Patients, Doctors, and Admins) with tailored interfaces and functionalities for each role.

**Last Updated**: January 14, 2026  


## ✨ Features

### Patient Features
- 📝 **Patient Registration & Authentication** - Secure signup and login
- 👤 **Profile Management** - View and edit personal information
- 📅 **Appointment Booking** - Schedule appointments with available doctors
- 👨‍⚕️ **View Doctors** - Browse available doctors and their specializations
- 📊 **Medical Records** - Access personal medical reports and history
- 💳 **Billing Information** - View billing details and charges

### Doctor Features
- 🔐 **Doctor Authentication** - Secure login portal
- 📋 **Appointment Management** - View and manage scheduled appointments
- 👥 **Patient List** - Access to registered patients
- 👤 **Profile Management** - Maintain professional profile information

### Admin Features
- 🛡️ **Admin Dashboard** - Comprehensive management interface
- 👨‍⚕️ **Doctor Management** - Add, update, and manage doctor profiles
- 💰 **Pricing Management** - Configure service pricing and billing
- 📊 **Appointment Approval** - Review and approve pending appointments
- 👥 **Patient Management** - View and manage all patient records

## 🛠️ Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Java |
| **Framework** | Android SDK (API 24-36) |
| **Backend** | Firebase Realtime Database |
| **Networking** | Retrofit 2.11.0 + OkHttp 4.12.0 |
| **Image Loading** | Glide 4.16.0 |
| **Image Upload** | ImgBB API |
| **JSON Parsing** | Gson |
| **Cloud Storage** | Firebase Storage 20.3.0 |
| **Build Tool** | Gradle (Kotlin DSL) |
| **Testing** | JUnit, Espresso |

## 📁 Project Structure

```
Ruhan_2207088_Hospital_Management_System-Android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/ruhan_2207088_hospital_management_system/
│   │   │   │   ├── Activities/
│   │   │   │   │   ├── MainActivity.java
│   │   │   │   │   ├── PatientLoginActivity.java
│   │   │   │   │   ├── PatientRegistrationActivity.java
│   │   │   │   │   ├── PatientDashboardActivity.java
│   │   │   │   │   ├── DoctorLoginActivity.java
│   │   │   │   │   ├── DoctorDashboard.java
│   │   │   │   │   ├── AdminLoginActivity.java
│   │   │   │   │   ├── AdminDashboardActivity.java
│   │   │   │   │   └── EditPatientProfileActivity.java
│   │   │   │   ├── Fragments/
│   │   │   │   │   ├── PatientProfileFragment.java
│   │   │   │   │   ├── BookAppointmentFragment.java
│   │   │   │   │   ├── MedicalReportsFragment.java
│   │   │   │   │   ├── PatientBillingFragment.java
│   │   │   │   │   ├── ViewDoctorsFragment.java
│   │   │   │   │   ├── DoctorAppointmentsFragment.java
│   │   │   │   │   ├── DoctorProfileFragment.java
│   │   │   │   │   ├── AddDoctorFragment.java
│   │   │   │   │   ├── AdminPricingFragment.java
│   │   │   │   │   ├── ApproveAppointmentsFragment.java
│   │   │   │   │   └── ViewPatientsFragment.java
│   │   │   │   ├── Models/
│   │   │   │   │   ├── Patient.java
│   │   │   │   │   ├── Doctor.java
│   │   │   │   │   ├── Appointment.java
│   │   │   │   │   ├── AppointmentModel.java
│   │   │   │   │   └── Pricing.java
│   │   │   │   ├── Adapters/
│   │   │   │   │   ├── PatientAdapter.java
│   │   │   │   │   ├── DoctorAdapter.java
│   │   │   │   │   ├── DoctorSelectAdapter.java
│   │   │   │   │   ├── AppointmentAdapter.java
│   │   │   │   │   ├── AppointmentApprovalAdapter.java
│   │   │   │   │   ├── BillingAdapter.java
│   │   │   │   │   └── PricingAdapter.java
│   │   │   │   ├── API/
│   │   │   │   │   ├── ImgBBApi.java
│   │   │   │   │   └── ImgBBResponse.java
│   │   │   │   └── Resources/
│   │   │   ├── res/ (UI resources - layouts, drawables, values)
│   │   │   └── AndroidManifest.xml
│   │   ├── test/ (Unit tests)
│   │   └── androidTest/ (Instrumented tests)
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
├── build.gradle.kts (Root)
├── settings.gradle.kts
├── gradle.properties
├── gradlew & gradlew.bat
└── .gitignore
```

## 🚀 Setup & Installation

### Prerequisites
- Android Studio (Latest version recommended)
- Java Development Kit (JDK 11 or higher)
- Android SDK (API level 24 and above)
- Gradle 8.0+

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/rahi-sadat/Ruhan_2207088_Hospital_Management_System-Android.git
   cd Ruhan_2207088_Hospital_Management_System-Android
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned repository folder
   - Click "Open"

3. **Sync Gradle Files**
   - Android Studio will prompt to sync Gradle files
   - Click "Sync Now"
   - Wait for all dependencies to download

4. **Configure Firebase** (if applicable)
   - Download your `google-services.json` from Firebase Console
   - Place it in the `app/` directory
   - Rebuild the project

5. **Run the Application**
   - Connect an Android device or start an emulator
   - Click the "Run" button (Shift + F10)
   - Select your device/emulator
   - The app will install and launch

## 📖 Usage Guide

### For Patients
1. **First Time Setup**
   - Tap "Register" on the login screen
   - Fill in your personal information
   - Create a secure password
   - Verify your details and complete registration

2. **Dashboard Navigation**
   - **Profile**: View and update personal information
   - **Book Appointment**: Schedule appointments with available doctors
   - **View Doctors**: Browse all registered doctors
   - **Medical Reports**: Access your medical history
   - **Billing**: View charges and payment status

3. **Booking an Appointment**
   - Go to "Book Appointment"
   - Select a doctor from the list
   - Choose preferred date and time
   - Submit the appointment request
   - Wait for admin approval

### For Doctors
1. **Login**
   - Use your doctor credentials
   - Access your personalized dashboard

2. **Manage Appointments**
   - View all scheduled appointments
   - See patient details
   - Update appointment status
   - Manage your availability

3. **Profile Management**
   - Update professional information
   - Manage specialization details

### For Administrators
1. **Login**
   - Use admin credentials
   - Access the admin dashboard

2. **Doctor Management**
   - Add new doctors to the system
   - Edit doctor profiles
   - Remove inactive doctors

3. **Appointment Approval**
   - Review pending appointments
   - Approve or reject requests
   - View appointment history

4. **Pricing Management**
   - Set consultation fees
   - Update service pricing
   - Manage billing tiers

## 🏗️ Architecture

The application follows **Fragment-based Architecture** with clear separation of concerns:

- **Activities**: Handle navigation and lifecycle management
- **Fragments**: Implement UI for specific features
- **Models**: Define data structures (Patient, Doctor, Appointment)
- **Adapters**: Bind data to RecyclerViews
- **API Clients**: Handle external service communication (ImgBB, Firebase)

## 📦 Dependencies

### Build Configuration
- **Compile SDK**: 36
- **Min SDK**: 24
- **Target SDK**: 36
- **Java Compatibility**: 11

### Key Libraries
```gradle
// Firebase Services
implementation("com.google.firebase:firebase-storage:20.3.0")
implementation(libs.firebase.database)

// Networking & Image Processing
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.github.bumptech.glide:glide:4.16.0")

// UI & Support Libraries
implementation(libs.appcompat)
implementation(libs.material)
implementation(libs.constraintlayout)
implementation(libs.activity)

// Testing
testImplementation(libs.junit)
androidTestImplementation(libs.ext.junit)
androidTestImplementation(libs.espresso.core)
```

## 👥 User Roles

| Role | Access Level | Key Functions |
|------|-------------|---|
| **Patient** | User Level | Register, Book Appointments, View Records, Check Billing |
| **Doctor** | Professional Level | View Appointments, Manage Schedule, Access Patient Info |
| **Admin** | System Level | Full System Management, Doctor/Patient Management, Approvals |

## 📱 App Permissions

The application requests the following permissions:
- `READ_MEDIA_IMAGES` - For image selection and upload
- `READ_EXTERNAL_STORAGE` - For accessing device files (deprecated on Android 13+)

## 🔧 Troubleshooting

**Gradle Sync Issues**
- Clear Gradle cache: `File > Invalidate Caches and Restart`
- Update Gradle: Edit `gradle.properties`

**Build Errors**
- Ensure Java 11+ is installed
- Check `build.gradle.kts` for dependency conflicts

**Firebase Connection**
- Verify `google-services.json` is in `app/` directory
- Check Firebase Console for project configuration

## 📝 License

This project is provided as-is for educational purposes.

