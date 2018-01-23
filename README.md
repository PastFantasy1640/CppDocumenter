# CppDocumenter
Cpp Docmenter

# Javadocと同じように
javadocと書式は一緒にしてあります。アルゴリズムは、/***/からその次の一行までをまとめるような感じです。

# コマンドの使い方

ごめんなさい、typoしてしまってプログラムはCppDocmentドクメントになってます。

```
java -jar CppDocmenter.jar [html_src] [from_dir] [to_dir] [extensions]
```

html_srcにはソースコードのひな型を置きます。ひな型で使える変数は%TITLE%と%LINKDIR%と%INDEX%と%CONTENT%です。

%TITLE%には先頭の要素のヘッダが入ります。何もないときは[Untitled]となりまｓ。

%LINKDIR%は、from_dirをルートとしての相対パスになります。スタイルシートと一緒に使えます。

%INDEX%は目次のようなものです。<section>~</section>で出力するので、<nav></nav>で囲ってあげるといいでしょう。

%CONTENT%は本文です。これも<section>~</section>で出力するので<article></article>などで囲ってあげるといいでしょう。

あとは仮出力してみて、適当にcssファイルを整備します。

from_dir、to_dirには、ソース元、そしてドキュメント出力先のフォルダを指定します。再帰的にその中を見て自動的に処理を進めます。

extensionsは変換する拡張子を指定できます。基本`.hpp`だと思いますが、複数含めたいときは`.hpp,.mel'などのように、カンマで区切ってください。
また拡張子である必要もなく、直接ファイル名を指定してあげることもできます。

# なによりも大事なこと

私が個人的に、本当に個人的に使えればいいなと思っているものです。
コードもまだリファクタリングしてないし、例外処理はprintStacktrace、ドキュメントも適当なので、いないと思うがもし使う勇者がいるなら自己責任で。

