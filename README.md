# Authy 
A simple authentication plugin for minecraft 1.17+!

[Also get it on spigot!](https://www.spigotmc.org/resources/authy.100004/)

## What is this plugin?

The plugins available on the market annoyed us so we decided to write our own plugin. It has the / remember command that remembers logging for 48 hours, securing multi-accounts per 1 IP address, a special pin system to increase account security and much more.

### Features

- Security of passwords and pins
- An additional function that is not required for players with the /pin command
- Fast and easy to set up
- The function of remembering login for 48 hours after entering /remember
- Completly customizable
- IP protection for multi-accounts

### Commands

`/login [password] [pin (required when toggled and set)]` - Logs in to the server

`/register [password] [password]` - Registers the player

`/unregister` - Unregisters the player

`/unregister [nickname]` - Unregisters given player (console only)

`/remember` - save login for 48h

`/pin set [pin]` - sets the pin

`/pin toggle` - toggle the pin

`/pin` - help command for pin's

`/authy reload` - reload configs and translations

`/authy` - info about the plugin


### Permissions

`authy.login` - permission for command /login

`authy.register` - permission for command /register

`authy.unregister` - permission for command /unregister

`authy.remember` - permission for command /remember

`authy.pin` - permissions for commands /pin

`authy.reload` - permission for command /authy reload

`authy.ipbypass` - bypass the ip checking

`authy.notifyonduplicateip` - notifications from duplicate ip accounts

## Useful Links

### [Default Config](https://github.com/Iru21/Authy/blob/master/src/main/resources/config.yml)

### [Creating Translations](https://github.com/Iru21/Authy/wiki/Translating-Authy-to-your-language)
[English template](https://github.com/Iru21/Authy/blob/master/src/main/resources/lang/en_us.yml)

### [Reporting Issues and giving suggestions](https://github.com/Iru21/Authy/issues)

## Used On

<img src="https://cdn.discordapp.com/attachments/855011517766697001/857656153223331851/reklama-poprawka2.png" width=700>
