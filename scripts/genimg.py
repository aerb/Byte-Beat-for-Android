import os

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)

dark_style = {
    "size": (50,50), 
    "color-replace": ("000000","333333")
}

light_style = {
    "size": (50,50), 
    "color-replace": ("000000","ffffff")
}

svgs = {
    "icon" : {"size":(512,512)},
    "add" : dark_style,
    "check" : dark_style,
    "controls" : dark_style,
    "copy" : dark_style,
    "cross" : dark_style,
    "folder" : dark_style,
    "paste" : dark_style,
    "pencil" : dark_style,
    "record" : dark_style,
    "refresh" : dark_style,
    "reset_args" : dark_style,
    "reset_t" : dark_style,
    "start" : dark_style,
    "stop" : dark_style,
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
    if exists(out_path):
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

for svg in svgs:
    compile_image(svg)