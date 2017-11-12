// You wrap the variable up inside a custom class such that the only access functions also trip the listener. So that the only way to flip the switch is via the functions which toggles and calls the listener.

public class BooVariable {
    private boolean boo = false;
    private ChangeListener listener;

    public boolean isBoo() {
        return boo;
    }

    public void setBoo(boolean boo) {
        this.boo = boo;
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
// To monitor the change you need to implement BooVariable.ChangeListener and then pass the BooVariable class a copy of "this" then, when you change the variable it calls onChange.

// Also, keeping in mind you can just inline the code rather than extend directly:

BooVariable bv = new BooVariable();
bv.setListener(new BooVariable.ChangeListener() {
    @Override
    public void onChange() {
        Toast.makeText(MainActivity.this,"blah", Toast.LENGTH_LONG).show();
     }
});
// PS. The toast must be called from the UI thread, so you need to switch the Looper if you're going to change the variable in a different thread. This likely won't come up though.
