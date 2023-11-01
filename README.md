## <img align="left" width="100" height="100" src="https://github.com/Iru21/Authy/assets/12859907/010ce25c-4e45-43b9-bf3e-3e7e698adc52">Authy — a simple authentication plugin for Minecraft 1.17+!

[<img alt="spigot" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/spigot_vector.svg">](https://www.spigotmc.org/resources/authy.100004/)
[<img alt="modrinth" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg">](https://modrinth.com/plugin/authy)

_Click one of the badges above to get it!_

### Features

- Security of passwords and pins
- Optional PIN system
- Session saving
- Customization
- Per IP multi-account detection
- MySQL support

...and more! ([suggestions are welcome!](https://github.com/Iru21/Authy/issues))

### Commands

| Command           | Arguments                                             | Description                                |
|-------------------|-------------------------------------------------------|--------------------------------------------|
| `/login`          | `[password] [pin (required when toggled and set)]`    | Logs in to the server                      |
| `/register`       | `[password] [repeat password]`                        | Registers the player                       |
| `/unregister`     |                                                       | Unregisters the player                     |
| `/unregister`     | `[nickname]`                                          | Unregisters given player (console only)    |
| `/remember`       |                                                       | Saves session for 48 hours (customizable)  |
| `/changepassword` | `[old password] [new password] [repeat new password]` | Changes your password                      |
| `/pin`            |                                                       | Help command for PINs                      |
| `/pin`            | `set [pin]`                                           | Sets the PIN                               |
| `/pin`            | `toggle`                                              | Toggles the PIN                            |
| `/authy`          |                                                       | Shows information about the plugin         |
| `/authy`          | `info`                                                | Shows selected information from the config |
| `/authy`          | `reload`                                              | Reloads config and translations            |

### Permissions

| Permission                  | Description                                      |
|-----------------------------|--------------------------------------------------|
| `authy.login`               | `/login` command                                 |
| `authy.register`            | `/register` command                              |
| `authy.unregister`          | `/unregister` command                            |
| `authy.remember`            | `/remember` command                              |
| `authy.pin`                 | `/pin` commands                                  |
| `authy.changepassword`      | `/changepassword` command                        |
| `authy.reload`              | `/authy reload` command                          |
| `authy.ipbypass`            | Bypass IP check                                  |
| `authy.notifyonduplicateip` | Notifications about accounts with duplicate IPs  |
| `authy.info`                | `/authy info` command                            |

<a href="https://bstats.org/plugin/bukkit/Authy/14475"><img src="https://bstats.org/signatures/bukkit/Authy.svg"></a>

## Useful Links

### [Default Config](https://github.com/Iru21/Authy/blob/master/src/main/resources/config.yml)

### [Creating translations](https://github.com/Iru21/Authy/wiki/Translating-Authy-to-your-language) ([English template](https://github.com/Iru21/Authy/blob/master/src/main/resources/lang/en_us.yml))

### [Reporting issues and giving suggestions](https://github.com/Iru21/Authy/issues)

## All thanks to our contributors!

<a href="https://github.com/Iru21/Authy/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Iru21/Authy" />
</a>
