import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const platform = MethodChannel('opencv_channel');
  String? imagePath;

  Future<void> processImage() async {
    try {
      final path = await platform.invokeMethod<String>('processImage');
      setState(() {
        imagePath = path;
      });
    } on PlatformException catch (e) {
      print("Error: ${e.message}");
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('Flutter + OpenCV')),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(
                onPressed: processImage,
                child: const Text('Process Image'),
              ),
              const SizedBox(height: 20),
              if (imagePath != null) Image.file(File(imagePath!)),
            ],
          ),
        ),
      ),
    );
  }
}