# boot-fmt: auto-format Clojure(Script) code

[![Clojars Project](https://img.shields.io/clojars/v/boot-fmt.svg)](https://clojars.org/boot-fmt)

A boot task for automatically reformatting your source code, similar to golang's
[gofmt](https://golang.org/cmd/gofmt/).

## Motivation

Beautiful machine-formatted Clojure code saves you time, helps you find bugs,
and avoids quibbles with collaborators. `gofmt`'s success has shown that
auto-formatting works for many golang teams; boot-fmt hopes to bring the same benefits
to Clojure(Script).

Powered by [zprint](https://github.com/kkinnear/zprint) and [clj-rewrite](https://github.com/xsc/rewrite-clj),
boot-fmt aims to provide a friendly command-line interface.

## Usage

With [boot](https://github.com/boot-clj/boot) installed, you can use boot-fmt from any directory:

```bash
boot -d boot-fmt/boot-fmt fmt -f src
```

Note that you do *not* need to use `boot` as your build tool or even have a
`build.boot` present in the current directory. As long as boot is installed in
your system, boot-fmt will work just fine even if your project is managed by
leiningen.

However, if you do use `boot` as your build tool, you may add
[boot-fmt's coordinates](https://clojars.org/boot-fmt) as a dependency to your
`build.boot`, e.g.:

```clojure
(set-env! :dependencies '[[boot-fmt/boot-fmt "X.Y.Z" :scope "test"]])
(require '[boot-fmt.core :refer [fmt]])
```

### Selecting files

There are three ways to select files to reformat. First, you can reformat all
clj(s) files in the current git repository:

```
boot fmt --git
```

If you want to reformat a project managed by `boot` and have already set up a
`build.boot` with `:source-paths` or `:resource-paths`, you can instruct
boot-fmt to use these as its search path:

```
boot fmt --source
```

Finally, you can manually specify one more or more files or directories to scan.
Directories are searched recursively for clj(s) source files:

```
boot fmt --files src
```

By specifying `--files` multiple times, you can add multiple files or
directories to the search path. The `--git`, `--source` and `--files` can be
combined.

### Operations

Like gofmt, boot-fmt defaults to printing reformatted file contents to standard
output. Generally boot-fmt can operate in four different modes: print, list,
diff and overwrite. You can specify the operation to perform using the `--mode`
parameter.

If your project is under source control (e.g. using git), a good option is to
let boot-fmt overwrite files in the current repository:

```
boot fmt --git --mode overwrite --really
```

You can use `git diff` or `git add -p` to see what was changed and to manually
confirm (or revert) reformated sections.

Note that overwriting files is potentially dangerous, so boot-fmt requires you
to add the `--really` flag to indicate that you know what you're doing.

To preview changes, diff mode can be useful. Instead of overwriting files, diff
mode prints a git-style diff to standard output:

```
boot fmt --git --mode diff
```

See the next section for a full description of each mode of operation.

### Command line options

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

## Changes

### 0.1.2

- Optionally scan for source files in current git repository
- Optionally scan for source files in boot's `:source-paths` and `:resource-paths`

## License

Copyright © 2016 Paulu Esterhazy

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
