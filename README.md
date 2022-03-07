# Graawk

A work-in-progress implementation of [AWK](https://en.wikipedia.org/wiki/AWK) on top of [GraalVM](https://www.graalvm.org/).

The eventual goal of this project is compatiblity with the [POSIX specification of awk](https://pubs.opengroup.org/onlinepubs/009604499/utilities/awk.html), with high performance on large, well-structured input files.

## Motivation

This is mostly a personal "for fun" project.

I heard about how [Truffle](https://www.graalvm.org/22.0/graalvm-as-a-platform/language-implementation-framework/) lets you write an interpreter for a language, and get a high-performance JIT runtime for "free."

At my day job (bioinformatics), I often use AWK to operate on gigabyte+ files that have a pretty regular structure. I think that a JIT could lend some really nice speedups there - automatically specializing at runtime for what fields matter to the AWK program, and the format of the fields themselves.

## Project Status

This code should be considered pre-alpha - don't use it for anything that matters.

This project was forked from [SimpleLanguage](https://github.com/graalvm/simplelanguage). Most of the internals still refer to the language as `SL`.

Very very simple AWK programs work, like `NR > 1 { print($NF); }`.
We also have decent function support (thanks SimpleLanguage!), like:

```awk
function fib(n) {
    if(n <= 1) { return 1; }
    return fib(n-1) + fib(n-2);
}
1 == 1 {print(fib(NR));}
```

Roadmap:

* [X] Basic math (integers only)
* [X] User function definition and invocation
* [X] AWK rule/action structure
* [X] Regex support
* [X] Partial `print` function support
* [X] Support field reads (`$1`, `$2`, ...)
* [X] Partial support for built-in variables (`NR`, `NF`, ...)
* [ ] Field writes (`$1 = "hello"`, ...)
* [ ] Built-in variable writes (`OFS = ","`, ...)
* [ ] `BEGIN` and `END` patterns
* [ ] Additional math operators (`x %= 2`, `2^4`, ...)
* [ ] Non-integer numeric types (float, double)
* [ ] Math functions (`sin`, `sqrt`, `rand`, ...)
* [ ] String functions (`length`, `sub`, `tolower`, ...)
* [ ] Arrays
* [ ] Input/output redirection (`getline < "myfile.txt"`, ...)
* [ ] `system` function for invoking other utils
* [ ] Implicit string concatentation (`"hello," "Ada" == "hello,Ada"`)
* [ ] Syntax conformation (e.g: call `print` without parens, optional semicolons at end of blocks)
* [ ] Implicit string->numeric casting for "numeric strings".
* [ ] Exit status code
* [ ] Command-line parameters
* [ ] Resolve syntactical differences (e.g: optional semicolons at end of blocks)
* [ ] Comb over operator precedence rules
* [ ] Locale support
* [ ] Fuzzing for errors
* [ ] Fuzzing for catching differences from other implementations
* [ ] Optimization
* [ ] More stuff that I've forgotten to list here
* [ ] If I go absolutely hog-wild - automatic parallelization?

## Performance

While it is much too early to make any bold claims, initial testing on the subset of AWK currently supported indicates that `graawk` can be faster (for some toy programs) than `gawk` or `mawk` on input sizes > 5,000,000 lines.

`graawk` is definitely slower than other AWK implementations for small data, and it will probably remain so.

## License

TBD! Eventually, I'd like to release all of this under some open-source license, but I haven't thought too hard about which specific one.

SimpleLanguage (whose code still forms the bulk of this project) is licensed under [UPL](https://www.oracle.com/downloads/licenses/upl-license.html). My own changes are not yet licensed, which as I understand it, means All Rights Reserved. If you're an individual, I'm probably not going to get mad at you for using it, but if you're making profit, I probably will be.

If you have opinions about licensing, let me know. :)

## Contributing

Disclaimer: I'm not interested in PRs or co-development at this early stage. The code is messy as heck, I've got a ton of weird ideas about where it's going, etc. If you really want to work on this, please contact me first.

That said, here are some instructions to develop or "install" the software:

1) Install GraalVM, version 22. (Higher versions may work.)
2) Install the necessary GraalVM plugins, like `native-image`.
3) Install Maven.
4) Run `mvn package`
5) Run `./sl myprogram.awk < input.txt > output.txt`

## FAQ

### How do I pronounce `graawk` ?

Kind of like a pterodactyl would. Or, you can pronounce it like [grok](https://en.wikipedia.org/wiki/Grok), which rhymes with AWK, at least in my dialect.
