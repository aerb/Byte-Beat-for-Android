import sys
import os

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)

dark_style = {
    "size": (100,100), 
    "color-replace": ("000000","333333")
}

light_style = {
    "size": (100,100), 
    "color-replace": ("000000","ffffff")
}

svgs = {
    "icon" : {"size":(512,512)},
    "add" : light_style,
    "check" : light_style,
    "controls" : light_style,
    "copy" : light_style,
    "cross" : light_style,
    "folder" : dark_style,
    "paste" : light_style,
    "pencil" : light_style,
    "record" : light_style,
    "refresh" : light_style,
    "reset_args" : light_style,
    "reset_t" : light_style,
    "start" : light_style,
    "stop" : light_style,
}

import inkscape
from os.path import join, exists

export_path=join('..','droidbeat','src','main','res','drawable')

def color_replace(svg_content, color):
    import re
    regex = re.compile(re.escape("#" + color[0]), re.IGNORECASE)
    return regex.sub("#" + color[1], svg_content)

def load_svg(filename):
    fp = open(filename)
    contents = fp.read()
    fp.close()
    return contents

def save_svg(content, filename):
    fp = open(filename, 'w')
    fp.write(content)
    fp.close()

def compile_image(key):
    out_path = join(export_path, key + ".generated.png")
    if exists(out_path) and not Force:
        print out_path + " up-to-date."
        return

    svg_path = key + ".svg"
    original = load_svg(svg_path)

    if "color-replace" in svgs[key]:
        colors = svgs[key]["color-replace"]
        temp = color_replace(original, colors)
        svg_path = "svg.tmp"
        save_svg(temp, svg_path)

    size = svgs[key]["size"]
    inkscape.compile(svg=svg_path,png=out_path, width=size[0], height=size[1])


Force = False
if len(sys.argv) > 1 and sys.argv[1] == "-f":
    Force = True

for svg in svgs:
    compile_image(svg)