# Electric Blocks

Electric Blocks is a Minecraft mod that simulates realistic power transmission.

[![Build][build-shield]][build-url]
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![License][license-shield]][license-url]

## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Installation](#installation)
  * [Prebuilt JAR](#prebuilt-jar)
  * [Manual Install](#manual-install)
* [Usage](#usage)
* [Contributing](#contributing)
* [License](#license)
* [Acknowledgements](#acknowledgements)

## About The Project

For all installation and usage information and other documentation, visit our [website](https://electricblocks.github.io).

The Electric Blocks mod brings accurate power flow simulation to Minecraft! Rather than using our own power flow implementation, Electric Blocks uses the heavily tested and validated PandaPower API. This means that the results of power flows are very accurate and can be used for real world modeling and testing of power flow. 

### Built With

* [MC Forge](http://files.minecraftforge.net/)
* [EBPP](https://github.com/ElectricBlocks/ebpp)

## Installation

To use this mod, you **must** have Forge version 1.15.2 **and** the [HWYLA](https://www.curseforge.com/minecraft/mc-mods/hwyla/files/2880069) mod installed. For single player, the power flow simulation server [EBPP](https://github.com/ElectricBlocks/ebpp) must be installed and running on your computer. For multiplayer, only the computer running the Forge server needs to have [EBPP](https://github.com/ElectricBlocks/ebpp) installed. As well, because Electric Blocks still uses Minecraft 1.15.2, if you're hosting a multiplayer server, it is vulnerable to the extremely dangerous Log4j exploit. More about that and how to fix it [here](https://help.minecraft.net/hc/en-us/articles/4416199399693-Security-Vulnerability-in-Minecraft-Java-Edition).

### Prebuilt JAR

First make sure you have MC Forge installed. If you want to play single player or host a multiplayer server then you will need to have EBPP installed and running as well. Then go to our releases page and download the latest release: https://github.com/ElectricBlocks/electricblocks/releases/

Move the JAR file into your mods folder and start up MC Forge.

### Building from Source

To build from sources, you will need:
* Java 8 JDK
* Gradle (included with source)

Once you have the prerequisites installed, run the following commands:
```sh
git clone https://github.com/ElectricBlocks/electricblocks.git
cd electricblocks
gradlew jar
```
We recommend using the included gradle wrapper. We are using version 4.10.3. Newer versions of gradle may cause this build to fail.

This may take a while. Once built, the JAR should be located in `./build/reobfJar/output.jar`.

Copy this JAR file into your mods folder and start MC Forge.

If compiling with an IDE, we recommend IntelliJ. If you encounter errors during compilation, you may need to add the HWYLA sources jar as a library.

## Usage

For usage info, check out our [website](https://electricblocks.github.io).

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

Distributed under the GNU Affero General Public License. See [LICENSE.md](LICENSE.md) for more information.

## Acknowledgements

This software has been developed by the following students at the University of Idaho for the 2020-21 and 2021-22 Senior Capstone Design courses:

* 21-22: Ryan Buckel - [gellyboi](https://github.com/gellyboi)
* 21-22: Samuel Frederickson - [SamFrederickson](https://github.com/SamFrederickson)
* 21-22: Greyson Biggs - [stlngds](https://github.com/stlngds)
* 20-21: Zachary Sugano - Project Lead - [zachoooo](https://github.com/zachoooo)
* 20-21: Christian Whitfield - Team Member/Communications Lead - [oceanwhit](https://github.com/oceanwhit)

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[build-shield]: https://img.shields.io/github/workflow/status/ElectricBlocks/electricblocks/Java%20CI%20with%20Gradle?style=flat-square
[build-url]: https://github.com/ElectricBlocks/electricblocks/actions?query=workflow%3A%22Java+CI+with+Gradle%22
[contributors-shield]: https://img.shields.io/github/contributors/ElectricBlocks/electricblocks.svg?style=flat-square
[contributors-url]: https://github.com/ElectricBlocks/electricblocks/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/ElectricBlocks/electricblocks.svg?style=flat-square
[forks-url]: https://github.com/ElectricBlocks/electricblocks/network/members
[stars-shield]: https://img.shields.io/github/stars/ElectricBlocks/electricblocks.svg?style=flat-square
[stars-url]: https://github.com/ElectricBlocks/electricblocks/stargazers
[issues-shield]: https://img.shields.io/github/issues/ElectricBlocks/electricblocks.svg?style=flat-square
[issues-url]: https://github.com/ElectricBlocks/electricblocks/issues
[license-shield]: https://img.shields.io/github/license/ElectricBlocks/electricblocks.svg?style=flat-square
[license-url]: https://github.com/ElectricBlocks/electricblocks/blob/master/LICENSE.md
