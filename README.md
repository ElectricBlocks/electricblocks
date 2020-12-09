# Electric Blocks

Electric Blocks is a minecraft mod that simulates realistic power transmission.

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

Electric Blocks is different from other Minecraft mods. Rather than using our own power flow implementation, this mod uses the heavily tested and validated PandaPower API. This means that the results of power flows are very accurate and can be used for real world modeling and testing of power flow. This tool can be used for educational or engineering purposes.

### Built With

* [MC Forge](http://files.minecraftforge.net/)
* [EBPP](https://github.com/ElectricBlocks/ebpp)

## Installation

To use this mod, you must have Forge version 1.15.2 installed. For single player, [EBPP](https://github.com/ElectricBlocks/ebpp) must be installed and running on your computer. For multiplayer, only the computer running the Forge server needs to have [EBPP](https://github.com/ElectricBlocks/ebpp) installed.

### Prebuilt JAR

First make sure you have MC Forge installed. If you want to play single player or host a multiplayer server then you will need to have EBPP installed and running as well. Then go to our releases page and download the latest release: https://github.com/ElectricBlocks/electricblocks/releases/

Move the JAR file into your mods folder and start up MC Forge.

### Manual Install

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

This may take a while. Once built, the JAR should be located in `./build/reobfJar/output.jar`

Copy this JAR file into your mods folder and start MC Forge.

## Usage

TODO: Create useage info

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

Distributed under the GNU Affero General Public License. See [LICENSE.md](LICENSE.md) for more information.

## Acknowledgements

This software is developed by students at the University of Idaho for the Capstone Design class:

* Zachary Sugano - Project Lead - [zachoooo](https://github.com/zachoooo)
* Christian Whitfield - Team Member/Communications Lead - [oceanwhit](https://github.com/oceanwhit)

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