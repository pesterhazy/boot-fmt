# boot-fmt: auto-format Clojure(Script) code

A boot task for automatically reformatting your source code, similar to golang's
[gofmt](https://golang.org/cmd/gofmt/).

`boot-fmt` is powered by [zprint](https://github.com/kkinnear/zprint).

NOTE: this is alpha quality software, use at your own risk

## Rationale

Coming soon

## Usage

```
boot fmt -f src
```

Print reformatted source code to standard output. Parameters specified using
`-f` can be files or directories. Directories are scanned recursively for
Clojure(Script) source files.

Specify the opeartion using the `--mode` paramter:

- `--mode print` (default): print reformatted code to standard output
- `--mode diff`: if reformatted code is different from original, print diff to
standard output.
- `--mode list`: where reformatted code is different from original, print filename
standard output.
- `--mode overwrite`: overwrite files with reformatted code. As this is a
  potentially dangerous operation, you need to specify the `--really` flag in
  addition to setting `--mode`.

## Author

`boot-fmt` is written by Paulus Esterhazy.
