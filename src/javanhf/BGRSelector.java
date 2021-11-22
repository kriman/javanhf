package javanhf;

public class BGRSelector implements Selector{
    public enum BGRvals {Blue, Green, Red};
    private BGRvals channel;
    public BGRSelector(BGRvals ch) {channel = ch;}
    @Override
    public int selector(byte b, byte g, byte r) {
        switch (channel) {
            case Blue:
                return b & 0xff;
            case Green:
                return g & 0xff;
            case Red:
                return r & 0xff;
            default:
                return 0;
        }
    }
}
