# DataDump

A multi-loader, multi-version Minecraft mod that dumps various game data during runtime into different file formats.

So far the data the mod can dump are the following:
- Blocks (including block states and block properties)
  - _Supported Output File Types:_ JSON, NBT
- Registries (including registry's elements codec)
  - _Supported Output File Types:_ JSON, NBT

## Usage

DataDump uses a profile-based system where you can configure what data to export and in what format.

Profiles are stored as TOML files in the `config/datadump` directory. You can create custom profiles or modify existing 
ones by simply creating new config files or editing existing ones.

The mod provides the following commands:
- `/datadump run <profile>` - Executes data dump using the specified profile
- `/datadump list` - Lists all available profiles
- `/datadump reset` - Resets preset profiles to default values  
- `/datadump help` - Shows the help message

## Building

This project uses Gradle and supports building for multiple mod loaders (Fabric, Forge, NeoForge) and Minecraft 
versions. To build the mod for a specific Minecraft version, you need to specify the `mc_ver` property.

For example, to build for Minecraft 1.21.1:
```bash
./gradlew build -Pmc_ver='1.21.1'
```

This will build the mod for all supported loaders (Fabric, Forge, NeoForge) for the specified Minecraft version.

## Version Compatibility

**Legend:** ✓ = Supported, ✗ = Not Supported, ? = Unknown/Untested

| Minecraft Version | Fabric | Forge | NeoForge |
|-------------------|--------|-------|----------|
| 1.21.10 | ? | ? | ? |
| 1.21.9 | ? | ? | ? |
| 1.21.8 | ? | ? | ? |
| 1.21.7 | ? | ? | ? |
| 1.21.6 | ? | ? | ? |
| 1.21.5 | ? | ? | ? |
| 1.21.4 | ? | ? | ? |
| 1.21.3 | ? | ? | ? |
| 1.21.2 | ? | ✗ | ? |
| 1.21.1 | ✓ | ? | ? |
| 1.21 | ? | ? | ? |
| 1.20.6 | ? | ? | ? |
| 1.20.5 | ? | ✗ | ? |
| 1.20.4 | ? | ? | ? |
| 1.20.3 | ? | ? | ? |
| 1.20.2 | ? | ? | ? |
| 1.20.1 | ? | ? | ✗ |
| 1.20 | ? | ? | ✗ |
| 1.19.4 | ? | ? | ✗ |
| 1.19.3 | ? | ? | ✗ |
| 1.19.2 | ? | ? | ✗ |
| 1.19.1 | ? | ? | ✗ |
| 1.19 | ? | ? | ✗ |
| 1.18.2 | ? | ? | ✗ |
| 1.18.1 | ? | ? | ✗ |
| 1.18 | ? | ? | ✗ |
| 1.17.1 | ? | ? | ✗ |
| 1.17 | ? | ✗ | ✗ |
| 1.16.5 | ? | ? | ✗ |
| 1.16.4 | ? | ? | ✗ |
| 1.16.3 | ? | ? | ✗ |
| 1.16.2 | ? | ? | ✗ |
| 1.16.1 | ? | ? | ✗ |
| 1.16 | ? | ✗ | ✗ |
| 1.15.2 | ? | ? | ✗ |
| 1.15.1 | ? | ? | ✗ |
| 1.15 | ? | ? | ✗ |
| 1.14.4 | ? | ? | ✗ |
| 1.14.3 | ? | ? | ✗ |
| 1.14.2 | ? | ? | ✗ |
| 1.14.1 | ? | ✗ | ✗ |
| 1.14 | ? | ✗ | ✗ |
| 1.13.2 | ✗ | ? | ✗ |
| 1.12.2 | ✗ | ? | ✗ |
| 1.12.1 | ✗ | ? | ✗ |
| 1.12 | ✗ | ? | ✗ |
| 1.11.2 | ✗ | ? | ✗ |
| 1.11 | ✗ | ? | ✗ |
| 1.10.2 | ✗ | ? | ✗ |
| 1.10 | ✗ | ? | ✗ |
| 1.9.4 | ✗ | ? | ✗ |
| 1.9 | ✗ | ? | ✗ |
| 1.8.9 | ✗ | ? | ✗ |
| 1.8.8 | ✗ | ? | ✗ |
| 1.8 | ✗ | ? | ✗ |
| 1.7.10 | ✗ | ? | ✗ |
| 1.7.2 | ✗ | ? | ✗ |
| 1.6.4 | ✗ | ? | ✗ |
| 1.6.2 | ✗ | ? | ✗ |
| 1.6.1 | ✗ | ? | ✗ |
| 1.5.2 | ✗ | ? | ✗ |
| 1.5.1 | ✗ | ? | ✗ |
| 1.4.7 | ✗ | ? | ✗ |
| 1.4.6 | ✗ | ? | ✗ |
| 1.4.5 | ✗ | ? | ✗ |
| 1.4.4 | ✗ | ? | ✗ |
| 1.4.2 | ✗ | ? | ✗ |
| 1.3.2 | ✗ | ? | ✗ |
| 1.2.5 | ✗ | ? | ✗ |
| 1.2.4 | ✗ | ? | ✗ |
| 1.2.3 | ✗ | ? | ✗ |
| 1.1 | ✗ | ? | ✗ |

## Developer Note

The base code for this mod was created with assistance from AI, which I then manually fixed, patched, modified, 
and improved. 

As for design decision for this project, I have decided to ensure that the `common` package has zero dependencies on 
the Minecraft library, including utility components like NBT handling. This architectural decision required abstracting 
all Minecraft-specific functionality, creating a clean separation between the mod's core logic and platform-specific 
implementations. This approach enhances portability across different mod loaders and Minecraft versions, while also 
making the codebase more maintainable and testable.

# Contact

I am primarily active on Discord, but my **DMs are closed**. The most effective way to get in touch with me is via a 
Discord Server **support ticket**.

To find the most up-to-date invite link to the Discord server, please visit my **Twitter/X** profile: [@Tyzeron](https://x.com/tyzeron).

I serve as a `System Administrator` on the Discord server and am highly active there. I have decided to take this 
approach as it allows me to keep my focus on mod development, without the added responsibility of owning and 
maintaining my own Discord server.
