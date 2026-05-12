# Visualization (شرح بصري مبسّط)

الهدف من الملف ده إن أي حد جديد يقدر يشوف صورة سريعة لطريقة الشغل داخل السيستم.
الرسومات مكتوبة بـ **Mermaid** علشان تظهر تلقائيًا في GitHub.

> **ملاحظة مهمة عن الذكاء الاصطناعي (AI):**
> المشروع الحالي لا يحتوي على نموذج ذكاء اصطناعي حقيقي. البحث والفلترة هنا منطق برمجي تقليدي (IF/LOOP).

---

## 1) Class Diagram (العلاقات بين الكلاسات)

```mermaid
classDiagram
    class User {
        <<abstract>>
        -String userId
        -String username
        -String name
        -String email
        -String password
        -String role
        +displayInfo() String
    }

    class Admin
    class Customer {
        -ArrayList~Rental~ rentalHistory
        +addRental(rental)
    }

    class Car
    class Rental
    class Payment
    class Invoice

    User <|-- Admin
    User <|-- Customer

    Rental --> Customer : has-a
    Rental --> Car : has-a

    Invoice --> Rental : has-a
    Invoice --> Payment : has-a
```

---

## 2) Booking Flow (رحلة الحجز خطوة بخطوة)

```mermaid
flowchart TD
    A[Customer يفتح شاشة السيارات] --> B[اختيار سيارة متاحة]
    B --> C[فتح نموذج الحجز BookingForm]
    C --> D[اختيار تاريخ البداية والنهاية]
    D --> E[حساب عدد الأيام والسعر]
    E --> F[تأكيد الحجز]
    F --> G[إنشاء Rental]
    G --> H[إنشاء Payment (محاكاة)]
    H --> I[إنشاء Invoice]
    I --> J[حفظ البيانات في DataStore + قاعدة البيانات]
    J --> K[عرض شاشة الفاتورة InvoiceScreen]
```

---

## 3) Login Flow (تسجيل الدخول)

```mermaid
flowchart TD
    L1[المستخدم يكتب Username/Password] --> L2{هل البيانات صحيحة؟}
    L2 -- نعم و Admin --> L3[AdminDashboard]
    L2 -- نعم و Customer --> L4[CustomerDashboard]
    L2 -- لا --> L5[رسالة خطأ]
```

---

## 4) GUI Navigation Map (خريطة تنقل الشاشات)

```mermaid
flowchart TD
    A[LoginScreen] -->|Admin| B[AdminDashboard]
    A -->|Customer| C[CustomerDashboard]

    B --> D[AddCarForm]
    B --> E[Rentals View]
    B --> F[Customers View]

    C --> G[BookingForm]
    C --> H[Rental History]
    C --> I[Invoices View]

    G --> J[InvoiceScreen]
    J --> C
    D --> B
```

---

## 5) LoginScreen (تفاصيل الشاشة)

```mermaid
flowchart TD
    L1[Title + Subtitle] --> L2[Username Field]
    L2 --> L3[Password Field]
    L3 --> L4[Login Button]
    L4 --> L5{Validation}
    L5 -- فارغ --> L6[Status: أدخل البيانات]
    L5 -- خطأ --> L7[Status: بيانات غير صحيحة]
    L5 -- Admin --> L8[AdminDashboard]
    L5 -- Customer --> L9[CustomerDashboard]
```

---

## 6) AdminDashboard (تفاصيل الشاشة)

```mermaid
flowchart TD
    A1[Top Bar: Welcome + Logout] --> A2[Nav Bar]
    A2 --> A3[Manage Cars View]
    A2 --> A4[All Rentals View]
    A2 --> A5[Customers View]

    A3 --> A6[Add Car]
    A3 --> A7[Edit Car]
    A3 --> A8[Delete Car]
    A6 --> A9[AddCarForm]
    A7 --> A9
```

---

## 7) AddCarForm (تفاصيل الشاشة)

```mermaid
flowchart TD
    F1[Brand / Model / Year / Price] --> F2[Type Combo]
    F2 --> F3[Save Button]
    F2 --> F4[Cancel Button]
    F3 --> F5{Validation}
    F5 -- خطأ --> F6[Status Label]
    F5 -- صحيح --> F7[Save to DB + DataStore]
    F7 --> F8[Back to AdminDashboard]
    F4 --> F8
```

---

## 8) CustomerDashboard (تفاصيل الشاشة)

```mermaid
flowchart TD
    C1[Top Bar: Welcome + Logout] --> C2[Nav Bar]
    C2 --> C3[Available Cars View]
    C2 --> C4[My Rentals View]
    C2 --> C5[My Invoices View]

    C3 --> C6[Search Bar]
    C3 --> C7[Price Filter]
    C3 --> C8[Cars Table]
    C3 --> C9[Book Selected Car]
    C9 --> C10[BookingForm]
```

---

## 9) BookingForm (تفاصيل الشاشة)

```mermaid
flowchart TD
    B1[Car Info] --> B2[Start Date]
    B2 --> B3[End Date]
    B3 --> B4[Payment Method]
    B4 --> B5[Auto Calc Days + Total]
    B5 --> B6[Confirm Booking]
    B5 --> B7[Cancel]
    B6 --> B8{Validation}
    B8 -- خطأ --> B9[Status Label]
    B8 -- صحيح --> B10[Create Rental + Payment + Invoice]
    B10 --> B11[InvoiceScreen]
    B7 --> B12[Back to CustomerDashboard]
```

---

## 10) InvoiceScreen (تفاصيل الشاشة)

```mermaid
flowchart TD
    I1[Invoice Header] --> I2[Rental Details]
    I2 --> I3[Payment Details]
    I3 --> I4[Back to Dashboard]
    I3 --> I5[Print / Save Invoice]
```
