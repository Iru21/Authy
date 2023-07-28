![Spiget Downloads](https://img.shields.io/spiget/downloads/100004?label=Spigot%20Downloads)

# Authy 
A simple authentication plugin for Minecraft 1.17+!

Also get it on [Spigot](https://www.spigotmc.org/resources/authy.100004/) and [Modrinth](https://modrinth.com/plugin/authy)!

### Features

- Security of passwords and pins
- Optional pin system
- Session saving
- Completely customizable
- Per IP multi-account detection
- MySQL support

### Commands

`/login [password] [pin (required when toggled and set)]` - Logs in to the server

`/register [password] [password]` - Registers the player

`/unregister` - Unregisters the player

`/unregister [nickname]` - Unregisters given player (console only)

`/remember` - Saves session for 48 hours (customizable)

`/changepassword [old password] [new password] [repeat new [password]` - Changes your password

`/pin set [pin]` - Sets the pin

`/pin toggle` - Toggles the pin

`/pin` - Help command for pins

`/authy reload` - Reloads config and translations

`/authy info` - Shows selected information from the config

`/authy` - Shows information about the plugin


### Permissions

`authy.login` - permission for command /login

`authy.register` - permission for command /register

`authy.unregister` - permission for command /unregister

`authy.remember` - permission for command /remember

`authy.pin` - permissions for commands /pin

`authy.changepassword` - permission for command /changepassword

`authy.reload` - permission for command /authy reload

`authy.ipbypass` - bypass the ip checking

`authy.notifyonduplicateip` - notifications from duplicate ip accounts

`authy.info` - permission for command /authy info

## Useful Links

### [Default Config](https://github.com/Iru21/Authy/blob/master/src/main/resources/config.yml)

### [Creating Translations](https://github.com/Iru21/Authy/wiki/Translating-Authy-to-your-language)
[English template](https://github.com/Iru21/Authy/blob/master/src/main/resources/lang/en_us.yml)

### [Reporting Issues and giving suggestions](https://github.com/Iru21/Authy/issues)
