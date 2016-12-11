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

Here's the output:

<!-- begin help -->```

Reformat Clojure(script) source files, like gofmt

Print reformatted source code to standard output. Parameters specified using
`-f` can be files or directories. Directories are scanned recursively for
Clojure(Script) source files.

Specify the operation using the --mode paramter:

--mode print (default)

Print reformatted code to standard output

--mode diff

When reformatted code is different from original, print diff to standard output.

--mode list

Where reformatted code is different from original, print filename standard output.

--mode overwrite

Overwrite files with reformatted code. As this is a potentially dangerous
operation, you need to specify the --really flag in addition to setting
the --mode parameter

Options:
  -h, --help          Print this help info.
  -m, --mode MODE     MODE sets mode of operation, i.e. print, list, diff or overwrite. Defaults to print.
  -r, --really        In overwrite mode, files are overwritten only if the --really flag is set as well
  -f, --files VAL     Conj VAL onto the list of files or directories to format
  -s, --source        Automatically scan for source files in boot source directories
  -g, --git           Automatically scan for source files in current git repository
  -o, --options OPTS  OPTS sets zprint options.

```
<!-- end help -->

## Configuration

You can supply a map of [zprint options](https://github.com/kkinnear/zprint#overview)
using the `--options` parameter:

```
boot fmt -f src --options '{:style :community, :fn-map {":require" :force-nl-body, "ns" :arg1-body}}'
```

## Author

`boot-fmt` is written by Paulus Esterhazy.
