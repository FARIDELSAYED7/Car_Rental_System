# Project Structure + OOP Overview

> الهدف: شرح هيكل المشروع بشكل واضح للمبتدئين مع فكرة سريعة عن الـ OOP.

---

## 1) Structure Tree

```
Car_Rental_System/
├─ README.md
├─ pom.xml
├─ text_uml.txt
├─ docs/
│  ├─ prd.md
│  ├─ project-structure.md
│  └─ visualization.md
├─ src/
│  ├─ app/
│  │  └─ Main.java
│  ├─ data/
│  │  ├─ DataStore.java
│  │  └─ DatabaseHelper.java
│  ├─ gui/
│  │  ├─ LoginScreen.java
│  │  ├─ AdminDashboard.java
│  │  ├─ CustomerDashboard.java
│  │  ├─ AddCarForm.java
│  │  ├─ BookingForm.java
│  │  └─ InvoiceScreen.java
│  └─ models/
│     ├─ User.java
│     ├─ Admin.java
│     ├─ Customer.java
│     ├─ Car.java
│     ├─ Rental.java
│     ├─ Payment.java
│     └─ Invoice.java
└─ target/ (build output)
```

---

## 2) What each folder does (ببساطة)

### `src/app`
- نقطة البداية للتطبيق.
- `Main.java` يشغل JavaFX ويفتح أول شاشة.

### `src/data`
- طبقة البيانات.
- `DataStore`: مخزن بيانات مؤقت في الذاكرة.
- `DatabaseHelper`: اتصال وتنفيذ أوامر MySQL.

### `src/gui`
- كل شاشات الواجهة الرسومية (JavaFX).

### `src/models`
- الكلاسات الأساسية (Entities) التي تمثل البيانات.

---

## 3) OOP Summary (مبسّط جدًا)

### ✅ Abstraction (تجريد)
- `User` كلاس مجرد (abstract) لا يمكن إنشاء Object منه مباشرة.

### ✅ Inheritance (وراثة)
- `Admin` و`Customer` يرثون من `User`.

### ✅ Encapsulation (تغليف)
- كل الحقول `private` ويتم الوصول إليها عن طريق getters/setters.

### ✅ Polymorphism (تعدد الأشكال)
- `displayInfo()` لها أكثر من تنفيذ حسب نوع المستخدم.

---

## 4) Data Flow (تبسيط لمسار البيانات)

- المستخدم يتعامل مع GUI.
- GUI تنادي DataStore/DatabaseHelper.
- DataStore يحدّث الذاكرة، وDatabaseHelper يحدّث قاعدة البيانات.
