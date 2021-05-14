import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

//untuk platform channel
const PLATFORM_CHANNEL = "com.midtrans.flutter";
const KEY_NATIVE = "showPaymentMidtrans";

class HalamanUtama extends StatefulWidget {
  @override
  _HalamanUtamaState createState() => _HalamanUtamaState();
}

class _HalamanUtamaState extends State<HalamanUtama> {
  static const platform = const MethodChannel(PLATFORM_CHANNEL);
  TextEditingController textEditingController = new TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(
        title: new Text("Midtrans Flutter"),
      ),
      body: Stack(
        children: [
          new Container(
            padding: EdgeInsets.all(11.0),
            child: new Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                new Text("Masukan Jumlah Barang"),
                new SizedBox(
                  height: 7.0,
                ),
                new TextField(
                  controller: textEditingController,
                  decoration: new InputDecoration(
                      hintText: "Silahkan Jumlah Barang",
                      border: new OutlineInputBorder(
                        borderRadius: new BorderRadius.circular(20.0),
                      )),
                )
              ],
            ),
          ),
          new Container(
            alignment: Alignment.bottomCenter,
            child: new Row(children: [
              new Expanded(
                child: new RaisedButton(
                  padding: EdgeInsets.all(12.0),
                  color: Colors.blueAccent,
                  splashColor: Colors.blue[800],
                  onPressed: () {
                    _showNative();
                  },
                  child: new Text(
                    "BELI",
                    style: new TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.w600,
                        fontSize: 21.0,
                        letterSpacing: 2.0),
                  ),
                ),
              ),
            ]),
          )
        ],
      ),
    );
  }

  Future<Null> _showNative() async {
    await platform.invokeMethod(KEY_NATIVE, {
      "nama": "Diky",
      "qty": "200",
      "price": "20000",
    });
  }
}
