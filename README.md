# 🩸 Rakta-Vahini — Filtered Blood Donor Network

> Connecting the right blood · to the right person · at the right time

A privacy-focused, GenAI-assisted Android app that replaces chaotic WhatsApp
blood requests with a smart, filtered donor directory — now with AI assistance
and multilingual support.

---

## 📌 Problem

When someone needs blood urgently at a rural hospital, the only option is
mass-forwarding on WhatsApp — no blood group filter, no eligibility check,
donor numbers publicly exposed, and critical minutes lost in the chaos.

---

## 💡 Solution

Rakta-Vahini shows **only the donors who can donate right now** by applying
three filters simultaneously:
| Filter | Logic |
|--------|-------|
| Blood Group | Exact match only |
| 90-Day Rule | Last donation must be > 90 days ago |
| GPS Radius | Within 10 km or 20 km of the request |

Phone numbers are **never shown on screen** — calls go through Android Intent.

---

## 📱 Screens

- **Home** — Greeting, blood group badge, eligibility status, stats
- **Emergency Search** — Filter by group + radius, one-tap secure call
- **My Profile** — Availability toggle, masked phone, donation count
- **Donation Log** — History, log new donation, auto thank-you notification
- **Ask AI** — GenAI chatbot powered by Anthropic Claude API

All screens support **Dark Mode / Light Mode** toggle.

---

## 🌐 Language Support

Rakta-Vahini supports multiple Indian languages with a one-tap toggle:

| Language | Code |
|----------|------|
| English  | `en` |
| Kannada  | `kn` |
| Hindi    | `hi` |

Language preference is saved locally and applied instantly across all
screens — no app restart required.

---

## 🤖 Ask AI — Powered by Gemini

The **Ask AI** screen lets donors and requesters get instant answers:

**What donors can ask:**
  - Can O+ donate to A-?
  - Who can donate blood?
  - What foods increase hemoglobin?
  - Is blood donation safe?
    
**How it works:**
```kotlin
// Send user message to Gemini API
val response = GeminiApiClient.sendMessage(
    systemPrompt = "You are a helpful blood donation assistant for Rakta-Vahini.",
    userMessage  = userInput,
    language     = selectedLanguage
)
```
---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Kotlin |
| Architecture | MVVM + Repository Pattern |
| Local Database | Room (SQLite) |
| Location | FusedLocationProviderClient + Haversine |
| Backend | Firebase Realtime Database |
| Authentication | Firebase Auth (Phone OTP)(Email Verification) |
| Notifications | Firebase Cloud Messaging (FCM) |
| Storage | Firebase Storage + Glide |
| GenAI / Ask AI | Anthropic Claude API |
| Multi-language | Android `strings.xml` + AppCompatDelegate |

---

## 🔑 Core Logic

```kotlin
// Eligibility check
val days = ChronoUnit.DAYS.between(lastDonationDate, LocalDate.now())
val isEligible = days > 90 && donor.isAvailable && donor.distanceKm <= radius

// Language switch (no restart needed)
AppCompatDelegate.setApplicationLocales(
    LocaleListCompat.forLanguageTags(selectedLanguageCode)
)
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android device / emulator (API 26+)
- Firebase project set up
- `google-services.json` placed in `/app`
- API key

### Setup

```bash
# 1. Clone the repository
git clone https://github.com/ansari0808/rakta-vahini.git

# 2. Open in Android Studio
File → Open → select the RaktaVahini folder

# 3. Add your google-services.json
Place it inside:  app/google-services.json

# 4. Add your keys in local.properties
GEMINI_API_KEY=your_api_key_here

# 5. Sync Gradle and Run
Click "Sync Now" → Run on emulator or device
```

---

## 🔥 Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create project → Register Android app (`com.raktavahini`)
3. Enable **Realtime Database**, **Authentication (Phone)**,
   **Storage**, **FCM**
4. Download `google-services.json` → place in `/app`
5. Set database rules to restrict writes to authenticated users only

---

## ✅ Success Criteria

| ID | Criterion | Result |
|----|-----------|--------|
| SC-01 | Ineligible donors hidden from search | Pass/Fail |
| SC-02 | Thank-you notification fires on log | Pass/Fail |
| SC-03 | Phone number never shown on screen | Pass/Fail |
| SC-04 | App works offline via Room DB | Pass/Fail |
| SC-05 | Language switches instantly, no restart | Pass/Fail |
| SC-06 | Ask AI responds in selected language | Pass/Fail |
| SC-07 | Professional, high-contrast UI | Design Review |

---

## 📁 Project Structure

````
RaktaVahini/
├── app/
│   └── src/main/
│       ├── java/com/raktavahini/
│       │   ├── model/           # Donor, DonationLog
│       │   ├── data/
│       │   │   ├── db/          # Room DAOs, AppDatabase
│       │   │   └── repository/  # DonorRepository
│       │   ├── viewmodel/       # DonorViewModel
│       │   ├── ui/
│       │   │   ├── home/        # HomeFragment
│       │   │   ├── search/      # SearchFragment, DonorAdapter
│       │   │   ├── profile/     # ProfileFragment
│       │   │   ├── log/         # LogFragment, LogAdapter
│       │   │   └── askai/       # AskAiFragment, ClaudeApiClient
│       │   └── utils/           # EligibilityUtils, DistanceUtils,
│       │                          NotificationUtils, SeedData,
│       │                          LanguageUtils
│       └── res/
│           ├── layout/          # All XML layouts
│           ├── navigation/      # nav_graph.xml
│           ├── values/          # colors, themes, strings (English)
│           ├── values-kn/       # strings (Kannada)
│           ├── values-hi/       # strings (Hindi)
│           ├── values-ta/       # strings (Tamil)
│           ├── values-te/       # strings (Telugu)
│           └── drawable/        # Shapes, icons
└── google-services.json         # ← Add your own (not committed)
````

---
**Registration Page**

---
<img width="367" height="778" alt="Screenshot 2026-05-12 at 7 16 50 PM" src="https://github.com/user-attachments/assets/b8627b20-b112-4b34-8931-44a6e5297799" />

---
**Login Page**

---
<img width="355" height="769" alt="Screenshot 2026-05-12 at 7 16 36 PM" src="https://github.com/user-attachments/assets/81db1511-d34d-4990-b5c8-8b229fcd84ca" />

---

**Home Page**

---
<img width="358" height="779" alt="Screenshot 2026-05-12 at 5 06 15 PM" src="https://github.com/user-attachments/assets/b875efeb-1d34-4972-8a7f-ed5ca0c761ae" />

---
**Emergency Search**

---
<img width="363" height="779" alt="Screenshot 2026-05-12 at 5 06 31 PM" src="https://github.com/user-attachments/assets/ebe0d996-85b9-42c0-bccf-edee6d8b8f67" />

---
**Ask AI**

---
<img width="355" height="771" alt="Screenshot 2026-05-12 at 5 06 43 PM" src="https://github.com/user-attachments/assets/e81bf4c4-8cfc-46d1-bd34-e83f39b2d3c3" />

---
**profile**

---
<img width="363" height="774" alt="Screenshot 2026-05-12 at 5 06 57 PM" src="https://github.com/user-attachments/assets/a3884315-0b32-40e7-9d1d-15d9204cf563" />

---
**Donation Log**

---
<img width="363" height="787" alt="Screenshot 2026-05-12 at 5 07 10 PM" src="https://github.com/user-attachments/assets/61b64604-610c-42be-adc3-c2360ba9441d" />


---
