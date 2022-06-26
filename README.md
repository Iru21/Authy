# Authy 
Minecraft 1.17+ için basit bir doğrulama eklentisi!

[Ayrıca Spigot'tan elde edebilirsiniz!](https://www.spigotmc.org/resources/authy.100004/)

## Nedir bu eklenti?

Piyasada bulunan eklentiler canımızı sıktı, böylece kendi eklentimizi yazmaya karar verdik. Eklenti 48 saat boyunca girişleri akılda tutabilen /remember komutuna, birçok hesabı 1 IP adresi altında güvencede tutma, hesap güvenliğini artırmak için özel PIN sistemine ve daha fazlasına sahip.

### Özellikler

- Şifre ve PIN'lerin güvenliği
- Oyuncular için gerekli olmayan /pin komutuyla ek bir fonksiyon
- Hızlı ve kolay kurulum
- /remember kullanıldıktan sonra 48 saatliğine girişleri kaydetme fonksiyonu
- Tamamen özelleştirilebilir
- Çoklu-hesap adına IP koruması

### Komutlar

`/login [şifre] [pin (etkinleştirildiğinde zorunludur)]` - Sunucuya giriş yapar

`/register [şifre] [şifre]` - Kullanıcıyı kaydeder

`/unregister` - Kullanıcının kaydını siler

`/unregister [kullanıcı adı]` - Verilen kullanıcının kaydını siler (konsola özel)

`/remember` - 48 saatliğine girişleri kaydeder

`/pin set [pin]` - PIN'i ayarlar

`/pin toggle` - PIN'i etkinleştirir/devre dışı bırakır

`/pin` - PIN'ler için yardım komutu

`/authy reload` - Yapılandırma ve çeviri dosyalarını yeniler

`/authy` - Eklenti hakkında bilgi


### Yetkiler

`authy.login` - /login komutu için yetki

`authy.register` - /register komutu için yetki

`authy.unregister` - /unregister komutu için yetki

`authy.remember` - /remember komutu için yetki

`authy.pin` - /pin komutu için yetki

`authy.reload` - /authy reload komutu için yetki

`authy.ipbypass` - IP kontrolünü es geç

`authy.notifyonduplicateip` - Kopyalanan IP adresleri için bildirimler

## İşe Yarar Bağlantılar

### [Varsayılan Yapılandırma Dosyası](https://github.com/Iru21/Authy/blob/master/src/main/resources/config.yml)

### [Çeviri Oluşturma](https://github.com/Iru21/Authy/wiki/Translating-Authy-to-your-language)
[İngilizce Taslak](https://github.com/Iru21/Authy/blob/master/src/main/resources/lang/en_us.yml)

### [Hata Bildirimleri ve Öneriler](https://github.com/Iru21/Authy/issues)

## Kullanıldığı Tarih

<img src="https://cdn.discordapp.com/attachments/855011517766697001/857656153223331851/reklama-poprawka2.png" width=700>
