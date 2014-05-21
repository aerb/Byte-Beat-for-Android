droidbeat_template = {
    "size":(50,50), 
    "color-replace":("000000","ffffff")
}

svgs = {
    "icon" : {"size":(512,512)},
    "add" : droidbeat_template,
    "check" : droidbeat_template,
    "controls" : droidbeat_template,
    "copy" : droidbeat_template,
    "cross" : droidbeat_template,
    "folder" : droidbeat_template,
    "paste" : droidbeat_template,
    "pencil" : droidbeat_template,
    "record" : droidbeat_template,
    "refresh" : droidbeat_template,
    "reset_args" : droidbeat_template,
    "reset_t" : droidbeat_template,
    "start" : droidbeat_template,
    "stop" : droidbeat_template,
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
    out_path = join(export_path, key + ".png")
    if exists(out_path):
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

