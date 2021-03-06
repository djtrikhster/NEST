// This test application accesses the Web site from Banco Santander, a Mexican bank, performs a login
// over SSL, receives a cookie and obtain the balance of a bank account, all using libcurl from Java

// import CurlGlue;
// import CurlWrite;

class test implements CurlWrite {

    public int handleString(byte s[])
    {
        /* output everything */
        System.out.println("IIIIIIIIIII -------------- OOOOOOOOOOOOOOOOOOO");
        try {

          System.out.write(s);

        }
        catch (java.io.IOException moo) {
          // nothing
        }
        return 0;
    }

    public static void main(String[] args)
    {

        // Bank account number and PIN - fake of course :)
        String account = new String("1234567");
        String pinCode = new String("1234");

        CurlGlue cg;

        try {
            test cw = new test();

            // Register callback write function
            cg = new CurlGlue();
            cg.setopt(CurlGlue.CURLOPT_WRITEFUNCTION, cw);

            // Login to the bank's secure Web site, posting account number and PIN code
            cg.setopt(CurlGlue.CURLOPT_URL, "https://www.santander.com.mx/SuperNetII/servlet/Login");
            cg.setopt(CurlGlue.CURLOPT_SSLVERSION, 3);
            cg.setopt(CurlGlue.CURLOPT_SSL_VERIFYPEER, 0); // attention! Insecure mode!!
            cg.setopt(CurlGlue.CURLOPT_VERBOSE, 1);
            cg.setopt(CurlGlue.CURLOPT_FOLLOWLOCATION, 1);
            cg.setopt(CurlGlue.CURLOPT_POST, 1);
            cg.setopt(CurlGlue.CURLOPT_COOKIEJAR, "cookie.txt");
            cg.setopt(CurlGlue.CURLOPT_POSTFIELDS, "pag=login&pagErr=/index.html&usuario="+account+"&clave="+pinCode+"&irAmodulo=1");
            cg.perform();

            // Access the bank account balance re-using the session ID stored in memory inside a cookie
            cg.setopt(CurlGlue.CURLOPT_URL, "https://www.santander.com.mx/SuperNetII/servlet/Cchequeras");
            cg.setopt(CurlGlue.CURLOPT_SSLVERSION, 3);
            cg.setopt(CurlGlue.CURLOPT_SSL_VERIFYPEER, 0); // attention! Insecure mode!!
            cg.setopt(CurlGlue.CURLOPT_VERBOSE, 1);
            cg.setopt(CurlGlue.CURLOPT_FOLLOWLOCATION, 1);
            cg.setopt(CurlGlue.CURLOPT_COOKIEFILE, "cookie.txt");
            cg.perform();

            // The cookie.txt file is actually created now
            cg.finalize();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


