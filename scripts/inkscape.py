from subprocess import call
def compile(svg, png, width, height):
    try:
        params = ["inkscape", svg, "--export-png=" + png]
        params.append("--export-width=" + str(width) + "px")
        params.append("--export-height=" + str(height) + "px")
        call(params)
    except OSError:
        print "Cannot find inkscape. Is it on the PATH?"
        raise