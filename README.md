# decomposition-viewer
**GUI application for [decomposition-library](https://github.com/YaaZ/decomposition-library)**

It is a Swing application with 2 render modes: AWT Graphics2D and native Vulkan rendering through JAWT.

You can **[download](https://github.com/YaaZ/decomposition-viewer/releases)** it, or [build](#how-to-build) it yourself.

## How to build
1. [Download](https://github.com/YaaZ/decomposition-viewer/releases) or [build](https://github.com/YaaZ/decomposition-viewer-jni)
native shared library
2. `mvn package` this project

Minimal required Java JDK version is 13.

## How to run
```
java -Djava.library.path="<path-to-native-library>" -jar decomposition-viewer-1.0-SNAPSHOT.jar [graphics2d]
```

## Fixes / Improvements
* [ ] Fix flickering on Intel GPUs (need to research, how Vulkan presentation interferes with JAWT & native surfaces)
