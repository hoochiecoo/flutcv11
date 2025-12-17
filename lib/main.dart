import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(const MaterialApp(home: OpenCVApp()));

class OpenCVApp extends StatefulWidget {
  const OpenCVApp({super.key});
  @override
  State<OpenCVApp> createState() => _OpenCVAppState();
}

class _OpenCVAppState extends State<OpenCVApp> {
  static const platform = MethodChannel('opencv_channel');
  String? _image;

  Future<void> _process() async {
    try {
      final String? path = await platform.invokeMethod('processImage');
      setState(() => _image = path);
    } catch (e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('OpenCV Fix v5')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            if (_image != null) Image.file(File(_image!), height: 300),
            ElevatedButton(onPressed: _process, child: const Text('Run OpenCV')),
          ],
        ),
      ),
    );
  }