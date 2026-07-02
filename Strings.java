public class Strings {

    // ========== LOGIN ==========
    public static final String LOGIN_TITLE = "فندق كاليفورنيا";
    public static final String LOGIN_SUBTITLE = "بوابة الإدارة";
    public static final String LOGIN_USERNAME = "اسم المستخدم";
    public static final String LOGIN_PASSWORD = "كلمة المرور";
    public static final String LOGIN_BUTTON = "تسجيل الدخول";
    public static final String LOGIN_WINDOW_TITLE = "فندق كاليفورنيا - تسجيل الدخول";
    public static final String LOGIN_ERROR_EMPTY = "يرجى إدخال اسم المستخدم وكلمة المرور";

    // ========== BRAND ==========
    public static final String BRAND_TITLE = "فندق كاليفورنيا";
    public static final String BRAND_SUBTITLE = "نظام الحجوزات";

    // ========== NAVIGATION ==========
    public static final String NAV_DASHBOARD = "لوحة التحكم";
    public static final String NAV_ROOMS = "الغرف";
    public static final String NAV_GUESTS = "النزلاء";
    public static final String NAV_BOOKINGS = "الحجوزات";
    public static final String NAV_USERS = "المستخدمون";
    public static final String NAV_LOGOUT = "تسجيل الخروج";

    // ========== DASHBOARD ==========
    public static final String DASHBOARD_WINDOW_TITLE = "فندق كاليفورنيا - لوحة التحكم";
    public static final String DASHBOARD_TITLE = "لوحة التحكم";
    public static final String DASHBOARD_TOTAL_ROOMS = "إجمالي الغرف";
    public static final String DASHBOARD_AVAILABLE_ROOMS = "الغرف المتاحة";
    public static final String DASHBOARD_TOTAL_BOOKINGS = "إجمالي الحجوزات";
    public static final String DASHBOARD_ACTIVE_BOOKINGS = "الحجوزات النشطة";

    // ========== ROOMS ==========
    public static final String ROOMS_TITLE = "إدارة الغرف";
    public static final String ROOMS_COL_ID = "الرقم";
    public static final String ROOMS_COL_NUMBER = "رقم الغرفة";
    public static final String ROOMS_COL_TYPE = "النوع";
    public static final String ROOMS_COL_PRICE = "السعر (د.ل)";
    public static final String ROOMS_COL_STATUS = "الحالة";
    public static final String ROOMS_LABEL_NUMBER = "رقم الغرفة:";
    public static final String ROOMS_LABEL_TYPE = "النوع:";
    public static final String ROOMS_LABEL_PRICE = "السعر (د.ل):";
    public static final String ROOMS_TYPE_SINGLE = "فردية";
    public static final String ROOMS_TYPE_DOUBLE = "زوجية";
    public static final String ROOMS_ADD = "إضافة غرفة";
    public static final String ROOMS_DELETE = "حذف المحدد";
    public static final String ROOMS_FILL_FIELDS = "يرجى ملء جميع حقول الغرفة";
    public static final String ROOMS_PRICE_NUMBER = "يجب أن يكون السعر رقماً";
    public static final String ROOMS_DUPLICATE_NUMBER = "رقم الغرفة موجود بالفعل";
    public static final String ROOMS_NO_SELECTION = "يرجى اختيار غرفة لحذفها";
    public static final String ROOMS_HAS_BOOKINGS = "لا يمكن حذف الغرفة: لديها حجوزات قائمة.\nقم بإلغائها أو إكمالها أولاً";
    public static final String ROOMS_STATUS_AVAILABLE = "متوفرة";
    public static final String ROOMS_STATUS_OCCUPIED = "محجوزة";
    public static final String ROOMS_CONFIRM_DELETE = "حذف الغرفة رقم {0}؟";

    // ========== GUESTS ==========
    public static final String GUESTS_TITLE = "إدارة النزلاء";
    public static final String GUESTS_COL_ID = "الرقم";
    public static final String GUESTS_COL_NAME = "الاسم";
    public static final String GUESTS_COL_NATIONAL_ID = "الهوية الوطنية";
    public static final String GUESTS_COL_PHONE = "الهاتف";
    public static final String GUESTS_LABEL_NAME = "الاسم:";
    public static final String GUESTS_LABEL_NATIONAL_ID = "الهوية الوطنية:";
    public static final String GUESTS_LABEL_PHONE = "الهاتف:";
    public static final String GUESTS_ADD = "إضافة نزيل";
    public static final String GUESTS_FILL_FIELDS = "يرجى ملء جميع حقول النزيل";
    public static final String GUESTS_NID_INVALID = "يجب أن تكون الهوية الوطنية من 6 أرقام بالضبط";
    public static final String GUESTS_PHONE_INVALID = "يجب أن يكون رقم الهاتف من 10 أرقام بالضبط";
    public static final String GUESTS_DUPLICATE_NID = "الهوية الوطنية مسجلة بالفعل";

    // ========== BOOKINGS ==========
    public static final String BOOKINGS_TITLE = "إدارة الحجوزات";
    public static final String BOOKINGS_COL_ID = "الرقم";
    public static final String BOOKINGS_COL_GUEST = "النزيل";
    public static final String BOOKINGS_COL_ROOM = "الغرفة";
    public static final String BOOKINGS_COL_CHECK_IN = "تاريخ الوصول";
    public static final String BOOKINGS_COL_CHECK_OUT = "تاريخ المغادرة";
    public static final String BOOKINGS_COL_STATUS = "الحالة";
    public static final String BOOKINGS_LABEL_GUEST = "النزيل:";
    public static final String BOOKINGS_LABEL_ROOM = "الغرفة:";
    public static final String BOOKINGS_LABEL_CHECK_IN = "تسجيل الوصول:";
    public static final String BOOKINGS_LABEL_CHECK_OUT = "تسجيل المغادرة:";
    public static final String BOOKINGS_CREATE = "إنشاء حجز";
    public static final String BOOKINGS_CHECK_IN = "تسجيل وصول";
    public static final String BOOKINGS_CANCEL = "إلغاء الحجز";
    public static final String BOOKINGS_CHECK_OUT = "تسجيل مغادرة";
    public static final String BOOKINGS_NO_GUESTS_ROOMS = "لا يوجد نزلاء أو غرف متاحة. أضفهم أولاً";
    public static final String BOOKINGS_INVALID_DATE = "يجب أن يكون تاريخ المغادرة بعد تاريخ الوصول";
    public static final String BOOKINGS_ERROR = "لم يتم العثور على النزيل أو الغرفة";
    public static final String BOOKINGS_SUCCESS = "تم إنشاء الحجز بنجاح! الرقم: {0}";
    public static final String BOOKINGS_NO_SELECTION = "يرجى اختيار حجز لإلغائه";
    public static final String BOOKINGS_NOT_ACTIVE = "يمكن إلغاء الحجوزات النشطة فقط";
    public static final String BOOKINGS_CONFIRM_CANCEL = "إلغاء الحجز رقم {0}؟";
    public static final String BOOKINGS_CANCELLED = "تم إلغاء الحجز";
    public static final String BOOKINGS_SELECT_FOR_CHECK_IN = "يرجى اختيار حجز لتسجيل الوصول";
    public static final String BOOKINGS_CHECK_IN_EMPTY = "يمكن تسجيل وصول الحجوزات النشطة فقط.\nالحالة الحالية: {0}";
    public static final String BOOKINGS_CHECK_IN_CONFIRM = "تسجيل وصول الحجز رقم {0}؟\nسيتم تعليم النزيل باعتباره CHECKED_IN";
    public static final String BOOKINGS_CHECK_IN_SUCCESS = "تم تسجيل الوصول بنجاح!\nالحجز {0} الآن CHECKED_IN";
    public static final String BOOKINGS_CHECK_OUT_EMPTY = "يرجى اختيار حجز لتسجيل المغادرة";
    public static final String BOOKINGS_CHECK_OUT_ONLY_CHECKED_IN = "يمكن تسجيل مغادرة الحجوزات التي تم تسجيل وصولها فقط.\nالحالة الحالية: {0}";
    public static final String BOOKINGS_CONFIRM_CHECK_OUT = "تسجيل مغادرة الحجز رقم {0}؟";
    public static final String BOOKINGS_CHECK_OUT_SUCCESS = "تم تسجيل المغادرة بنجاح";

    // ========== USERS ==========
    public static final String USERS_TITLE = "إدارة المستخدمين";
    public static final String USERS_COL_ID = "الرقم";
    public static final String USERS_COL_USERNAME = "اسم المستخدم";
    public static final String USERS_COL_ROLE = "الدور";
    public static final String USERS_LABEL_USERNAME = "اسم المستخدم:";
    public static final String USERS_LABEL_PASSWORD = "كلمة المرور:";
    public static final String USERS_LABEL_ROLE = "الدور:";
    public static final String USERS_ADD = "إضافة مستخدم";
    public static final String USERS_DELETE = "حذف المحدد";
    public static final String USERS_FILL_FIELDS = "يرجى ملء جميع حقول المستخدم";
    public static final String USERS_DUPLICATE_USERNAME = "اسم المستخدم محجوز بالفعل";
    public static final String USERS_ADD_SUCCESS = "تمت إضافة المستخدم بنجاح";
    public static final String USERS_NO_SELECTION = "يرجى اختيار مستخدم لحذفه";
    public static final String USERS_SELF_DELETE = "لا يمكنك حذف نفسك!";
    public static final String USERS_ROLE_ADMIN = "مسؤول";
    public static final String USERS_ROLE_RECEPTIONIST = "موظف استقبال";
    public static final String USERS_CONFIRM_DELETE = "حذف المستخدم رقم {0}؟";

    // ========== REPORT ==========
    public static final String REPORT_PRINT = "طباعة تقرير";
    public static final String REPORT_SAVED = "تم حفظ التقرير في: {0}";
    public static final String REPORT_ERROR = "خطأ في حفظ التقرير: {0}";

    // ========== DIALOGS ==========
    public static final String DIALOG_VALIDATION_ERROR = "خطأ في التحقق";
    public static final String DIALOG_ERROR = "خطأ";
    public static final String DIALOG_SUCCESS = "نجاح";
    public static final String DIALOG_WARNING = "تحذير";
    public static final String DIALOG_CONFIRM = "تأكيد";
    public static final String DIALOG_NO_SELECTION = "لم يتم الاختيار";
    public static final String DIALOG_DUPLICATE_ERROR = "خطأ التكرار";
    public static final String DIALOG_INVALID_ACTION = "إجراء غير صالح";
    public static final String DIALOG_DB_ERROR = "خطأ في قاعدة البيانات: {0}";
}
