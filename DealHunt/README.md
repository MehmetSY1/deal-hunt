# DealHunt — Android Kurulum Rehberi

## Gereksinimler
- Android Studio Hedgehog veya daha yeni
- JDK 17
- Android SDK 24+

---

## 1. Adım — Android Studio'yu İndir

https://developer.android.com/studio adresinden indir ve kur.

---

## 2. Adım — Projeyi Aç

1. Android Studio'yu aç
2. "Open" → Bu klasörü seç (DealHunt/)
3. Gradle sync otomatik başlar, bekle (~2 dakika)

---

## 3. Adım — Fontları Ekle (Önemli!)

Android Studio içinde:
1. `app/src/main/res/font/` klasörüne sağ tıkla
2. New → Font resource file seç
3. Aşağıdaki fontları Google Fonts'tan indir ve klasöre koy:

| Dosya Adı | Font | Ağırlık |
|-----------|------|---------|
| `rajdhani_bold.ttf` | Rajdhani | Bold 700 |
| `rajdhani_semibold.ttf` | Rajdhani | SemiBold 600 |
| `dm_sans_regular.ttf` | DM Sans | Regular 400 |

İndir: https://fonts.google.com/specimen/Rajdhani
İndir: https://fonts.google.com/specimen/DM+Sans

---

## 4. Adım — Cihaza Kur

### Gerçek Telefon (Önerilen):
1. Telefonda: Ayarlar → Geliştirici Seçenekleri → USB Hata Ayıklama → Aç
2. USB ile bilgisayara bağla
3. Android Studio'da yeşil ▶ "Run" butonuna bas

### Emülatör:
1. Android Studio → Tools → Device Manager
2. "Create Device" → Pixel 6 → API 34
3. ▶ Run butonuna bas

---

## 5. Adım — APK Oluştur (Paylaşmak için)

```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```

APK dosyası şuraya oluşur:
`app/build/outputs/apk/debug/app-debug.apk`

Bu dosyayı telefona kopyalayıp yükleyebilirsin.

---

## Uygulama Özellikleri

### Ana Ekran
- **Canlı Arama**: 30.000+ oyun, CheapShark API üzerinden
- **Steam, Epic Games, GOG, Humble Bundle** ve 30+ platform
- **En İyi Fırsatlar**: Anlık güncellenen indirimler
- **Aşağı çek-yenile**: Fiyatları anında yenile

### Oyun Detay Ekranı
- Tüm platformların fiyatları yan yana
- En iyi fiyat yeşil olarak öne çıkar
- İndirim yüzdesi ve orijinal fiyat
- Tarihteki en ucuz fiyat
- "Satın Al" butonu → direkt platforma yönlendirir

### API Hakkında
Uygulama **CheapShark API** kullanır:
- Tamamen ücretsiz, API anahtarı gerekmez
- Steam, Epic, GOG, Humble Bundle, Fanatical ve 30+ platform
- Gerçek zamanlı fiyat verisi
- Dökümantasyon: https://apidocs.cheapshark.com

---

## Sorun Giderme

**"Sync failed"** hatası:
→ File → Invalidate Caches → Restart

**Font hatası**:
→ Fontları res/font/ klasörüne ekle

**İnternet hatası**:
→ AndroidManifest.xml'de INTERNET permission var, emülatörde internet olduğundan emin ol

---

## Geliştirme Notları

```
DealHunt/
├── app/src/main/java/com/dealhunt/app/
│   ├── data/
│   │   ├── CheapSharkApi.kt     ← Retrofit API tanımları
│   │   └── GameRepository.kt    ← Veri katmanı
│   ├── model/
│   │   └── Models.kt            ← Veri modelleri
│   └── ui/
│       ├── MainActivity.kt      ← Ana ekran
│       ├── DetailActivity.kt    ← Oyun detay
│       ├── MainViewModel.kt     ← Ana ekran mantığı
│       ├── DetailViewModel.kt   ← Detay mantığı
│       ├── SearchResultAdapter.kt
│       ├── FeaturedDealAdapter.kt
│       └── PlatformPriceAdapter.kt
└── app/src/main/res/
    ├── layout/                  ← Ekran tasarımları
    ├── drawable/                ← Görseller
    ├── values/                  ← Renkler, stiller
    └── font/                    ← Fontlar (sen ekleyeceksin)
```
