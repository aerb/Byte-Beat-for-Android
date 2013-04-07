PATH=$PATH:'/cygdrive/c/Program Files (x86)/Inkscape'

BASE_COLOR='#000000'


SVG_FOLDER='svgs'
LAYOUT_FOLDER='../res/drawable'
PNG_FOLDER='../res/drawable'

mkdir $SVG_FOLDER -p
mkdir $LAYOUT_FOLDER -p
mkdir $PNG_FOLDER -p

ICONS=('controls' 'folder' 'start' 'refresh' 'check' 'cross' 'add' 'copy' 'pencil')
for ICON in "${ICONS[@]}"
do
	echo Exporting $ICON
	
	ICON_PRESS=$ICON\_press
	ICON_DISABLE=$ICON\_disable
	ICON_WHITE=$ICON\_white

	BASE_SVG=$ICON.svg
	SVG_STANDARD=$SVG_FOLDER/$ICON.svg
	SVG_PRESSED=$SVG_FOLDER/$ICON_PRESS.svg
	SVG_DISABLED=$SVG_FOLDER/$ICON_DISABLE.svg
	SVG_WHITE=$SVG_FOLDER/$ICON_WHITE.svg

	PNG_STANDARD=$PNG_FOLDER/$ICON.png
	PNG_PRESSED=$PNG_FOLDER/$ICON_PRESS.png
	PNG_DISABLED=$PNG_FOLDER/$ICON_DISABLE.png
	PNG_WHITE=$PNG_FOLDER/$ICON_WHITE.png

	LAYOUT_TEMPLATE=android\_button\_template.xml
	GENERATED_LAYOUT=$LAYOUT_FOLDER/$ICON\_button\_style.xml
	GENERATED_WHITE_LAYOUT=$LAYOUT_FOLDER/$ICON\_button\_white\_style.xml

	sed 's/fill:'$BASE_COLOR'/fill:#333333/g' $BASE_SVG > $SVG_STANDARD
	sed 's/fill:'$BASE_COLOR'/fill:#ffc83d/g' $BASE_SVG > $SVG_PRESSED
	sed 's/fill:'$BASE_COLOR'/fill:#909090/g' $BASE_SVG > $SVG_DISABLED
	sed 's/fill:'$BASE_COLOR'/fill:#ffffff/g' $BASE_SVG > $SVG_WHITE

	sed -e 's/STANDARD_STATE/'$ICON'/g' -e 's/DISABLED_STATE/'$ICON_DISABLE'/g' -e 's/PRESSED_STATE/'$ICON_PRESS'/g' $LAYOUT_TEMPLATE > $GENERATED_LAYOUT
	sed -e 's/STANDARD_STATE/'$ICON_WHITE'/g' -e 's/DISABLED_STATE/'$ICON_DISABLE'/g' -e 's/PRESSED_STATE/'$ICON_PRESS'/g' $LAYOUT_TEMPLATE > $GENERATED_WHITE_LAYOUT

	inkscape.com $SVG_STANDARD --export-png=$PNG_STANDARD --export-width=200px --export-background-opacity=0
	inkscape.com $SVG_PRESSED --export-png=$PNG_PRESSED --export-width=200px --export-background-opacity=0 
	inkscape.com $SVG_DISABLED --export-png=$PNG_DISABLED --export-width=200px --export-background-opacity=0 
	inkscape.com $SVG_WHITE --export-png=$PNG_WHITE --export-width=200px --export-background-opacity=0
done