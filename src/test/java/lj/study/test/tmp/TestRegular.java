package lj.study.test.tmp;

import org.testng.annotations.Test;

public class TestRegular {

    @Test
    public void test_regular() {
        String s = "abcd";
        String p = "ab";
//        Pattern pattern = new Pattern();
//        Matcher matcher = new Matcher();
        System.out.println(s.charAt(0));
        System.out.println(s.charAt(2));
    }


    public boolean isMatch1(String s, String p) {
        int m = s.length();
        int n = p.length();

        boolean[][] f = new boolean[m + 1][n + 1];
        f[0][0] = true;
        for (int i = 0; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (p.charAt(j - 1) == '*') {
                    f[i][j] = f[i][j - 2];
                    if (matches1(s, p, i, j - 1)) {
                        f[i][j] = f[i][j] || f[i - 1][j];
                    }
                } else {
                    if (matches1(s, p, i, j)) {
                        f[i][j] = f[i - 1][j - 1];
                    }
                }
            }
        }

        for(int i=0; i<m+1; i++){
            for(int j=0; j<n+1; j++){
                System.out.print(f[i][j] + "  ");
            }
            System.out.println(" ----");
        }
        return f[m][n];
    }

    public boolean matches1(String s, String p, int i, int j) {
        if (i == 0) {
            return false;
        }
        if (p.charAt(j - 1) == '.') {
            return true;
        }
        return s.charAt(i - 1) == p.charAt(j - 1);
    }


    /**
     * 动态规划求解字符串和正则表达式是否匹配
     *
     * @param s:字符串
     * @param p:模式串，可包含 . *
     * @return
     */
    public boolean isMatch2(String s, String p) {
        int m = s.length();
        int n = p.length();
        //动态规划，数组，记录每个阶段结果。默认都为false。+1 是因为包含空s、空p
        boolean[][] t = new boolean[m+1][n+1];
        t[0][0] = true;

        if(p.length() == 0){
            return s.length() == 0;
        }

        for(int i=0; i<=m; i++){ // s可为空串
            for(int j=1; j<=n; j++){ // p为空串前置已经执行过，此处不可能为空
                if(p.charAt(j-1) == '*'  && j>=2){ //p的最后2位为1组
                    if(i>=1
                            && (p.charAt(j-2) == s.charAt(i-1) || p.charAt(j-2) == '.')){ //匹配1次以上
                        t[i][j] = t[i-1][j] || t[i][j-2];
                    }else{ //匹配0次
                        t[i][j] = t[i][j-2];
                    }
                }else if(i>=1 && p.charAt(j-1) == '.' ){
                    t[i][j] = t[i-1][j-1];
                }else {
                    if(i>=1 && p.charAt(j-1) == s.charAt(i-1)){
                        t[i][j] = t[i-1][j-1];
                    }
                }
            }
        }
        for(int i=0; i<m+1; i++){
            for(int j=0; j<n+1; j++){
                System.out.print(t[i][j] + "  ");
            }
            System.out.println(" ----");
        }

        return t[m][n];
    }


    private int fibonacci(int n) {
        if (n == 1) return 0;
        if (n == 2) return 1;
//        else return fibonacci(n - 1) + fibonacci(n - 2);
        else {
            int[] r = new int[n];
            r[0] = 0;
            r[1] = 1;
            for(int i=2; i<n; i++){
                r[i] = r[i-1] + r[i-2];
            }
            return r[n-1];
        }
    }

    @Test
    public void test(){
//        Assert.assertEquals(isMatch2("abcd", "abc."), true);
        boolean r1 = isMatch1("", "c*ab");
        System.out.println(">>>>>>>>>>>>>>>>");
        boolean r2 = isMatch2("", "c*ab");

//        Assert.assertEquals(isMatch1("ab", "a.*"), true);
//
//        Assert.assertEquals(isMatch2("ab", "a.*"), true);
//        Assert.assertEquals(isMatch2("abcd", "abcd"), true);
//        Assert.assertEquals(isMatch2("abcd", "abcd.*"), true);
    }

}
