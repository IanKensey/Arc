package arc.scene.ui.layout;

import arc.struct.SnapshotArray;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.Scene;
import arc.scene.utils.Layout;

/**
 * A {@link Group} that participates in layout and provides a minimum, preferred, and maximum size.
 * <p>
 * The default preferred size of a widget group is 0 and this is almost always overridden by a subclass. The default minimum size
 * returns the preferred size, so a subclass may choose to return 0 for minimum size if it wants to allow itself to be sized
 * smaller than the preferred size. The default maximum size is 0, which means no maximum size.
 * <p>
 * See {@link Layout} for details on how a widget group should participate in layout. A widget group's mutator methods should call
 * {@link #invalidate()} or {@link #invalidateHierarchy()} as needed. By default, invalidateHierarchy is called when child widgets
 * are added and removed.
 * @author Nathan Sweet
 */
public class WidgetGroup extends Group implements Layout{
    private boolean needsLayout = true;
    private boolean fillParent;
    private boolean layoutEnabled = true;

    public WidgetGroup(){
    }

    /** Creates a new widget group containing the specified actors. */
    public WidgetGroup(Element... actors){
        for(Element actor : actors)
            addChild(actor);
    }

    @Override
    public float getMinWidth(){
        return getPrefWidth();
    }

    @Override
    public float getMinHeight(){
        return getPrefHeight();
    }

    @Override
    public float getPrefWidth(){
        return 0;
    }

    @Override
    public float getPrefHeight(){
        return 0;
    }

    @Override
    public void setLayoutEnabled(boolean enabled){
        if(layoutEnabled == enabled) return;
        layoutEnabled = enabled;
        setLayoutEnabled(this, enabled);
    }

    private void setLayoutEnabled(Group parent, boolean enabled){
        SnapshotArray<Element> children = parent.getChildren();
        for(int i = 0, n = children.size; i < n; i++){
            Element actor = children.get(i);
            if(actor instanceof Layout)
                actor.setLayoutEnabled(enabled);
            else if(actor instanceof Group) //
                setLayoutEnabled((Group)actor, enabled);
        }
    }

    @Override
    public void validate(){
        if(!layoutEnabled) return;

        Group parent = getParent();
        if(fillParent && parent != null){
            float parentWidth, parentHeight;
            Scene stage = getScene();
            if(stage != null && parent == stage.root){
                parentWidth = stage.getWidth();
                parentHeight = stage.getHeight();
            }else{
                parentWidth = parent.getWidth();
                parentHeight = parent.getHeight();
            }
            if(getWidth() != parentWidth || getHeight() != parentHeight){
                setWidth(parentWidth);
                setHeight(parentHeight);
                invalidate();
            }
        }

        if(!needsLayout) return;
        needsLayout = false;
        layout();
    }

    /** Returns true if the widget's layout has been {@link #invalidate() invalidated}. */
    @Override
    public boolean needsLayout(){
        return needsLayout;
    }

    @Override
    public void invalidate(){
        needsLayout = true;
    }

    @Override
    public void invalidateHierarchy(){
        invalidate();
        Group parent = getParent();
        if(parent != null) parent.invalidateHierarchy();
    }

    @Override
    protected void childrenChanged(){
        invalidateHierarchy();
    }

    @Override
    protected void sizeChanged(){
        invalidate();
    }

    @Override
    public void pack(){
        setSize(getPrefWidth(), getPrefHeight());
        validate();
        //Some situations require another layout. Eg, a wrapped label doesn't know its pref height until it knows its width, so it
        //calls invalidateHierarchy() in layout() if its pref height has changed.
        if(needsLayout){
            setSize(getPrefWidth(), getPrefHeight());
            validate();
        }
    }

    @Override
    public void setFillParent(boolean fillParent){
        this.fillParent = fillParent;
    }

    @Override
    public void layout(){
    }

    /**
     * If this method is overridden, the super method or {@link #validate()} should be called to ensure the widget group is laid
     * out.
     */
    @Override
    public void draw(){
        validate();
        super.draw();
    }
}
