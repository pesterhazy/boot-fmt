# boot-fmt: auto-format Clojure(Script) code

[![Clojars Project](https://img.shields.io/clojars/v/boot-fmt.svg)](https://clojars.org/boot-fmt)

A boot task for automatically reformatting your source code, similar to golang's
[gofmt](https://golang.org/cmd/gofmt/).

`boot-fmt` is powered by [zprint](https://github.com/kkinnear/zprint).

NOTE: this is **alpha quality** software

## Rationale

Coming soon

## Usage

With [boot](https://github.com/boot-clj/boot) installed, you can use `boot-fmt` anywhere:

```bash
boot -d boot-fmt/boot-fmt fmt -f src
```

Not that this does *not* require you to use `boot` as your build tool or even to
have a `build.boot` present in the current directory. However, if you do
use `boot` as your build tool, you may add
[boot-fmt's coordinates](https://clojars.org/boot-fmt) as a dependency to your
`build.boot`, e.g.:

```clojure
(set-env! :dependencies '[[boot-fmt/boot-fmt "X.Y.Z" :scope "test"]])
(require '[boot-fmt.core :refer [fmt]])
```

You can see the options available on the command line:

```bash
boot -d boot-fmt/boot-fmt fmt --help
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
