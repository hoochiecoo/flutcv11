import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(const MaterialApp(home: OpenCVTest()));

class OpenCVTest extends StatefulWidget {
  const OpenCVTest({super.key});
  @override
  State<OpenCVTest> createState() => _OpenCVTestState();
}

class _OpenCVTestState extends State<OpenCVTest> {
  static const platform = MethodChannel('opencv_channel');
  String? _path;

  Future<void> _process() async {
    try {
      final String? result = await platform.invokeMethod('processImage');
      setState(() => _path = result);
    } catch (e) {
      debugPrint("Error: $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('OpenCV AndroidX Fix')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            if (_path != null) Image.file(File(_path!), height: 300),
            ElevatedButton(onPressed: _process, child: const Text('Process Image')),
          ],
        ),
      ),
    );
  }