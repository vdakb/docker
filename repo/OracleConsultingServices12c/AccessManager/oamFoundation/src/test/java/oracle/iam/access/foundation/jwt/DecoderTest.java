package oracle.iam.access.foundation.jwt;

import java.util.Map;
import java.util.HashMap;

import java.io.File;

public class DecoderTest {
//  private static String token = "eyJraWQiOiJkZWZhdWx0IiwieDV0IjoiME4zaHdXVXlUZEVkdHpuSHctemY2RzQzX0tVIiwiYWxnIjoiUlMyNTYifQ.eyJleHAiOjE1NDA0MDEwNzQsImp0aSI6Im5sT1RGZDRMbG5ZaDBacnBWMkRIUnciLCJpYXQiOjE1NDAzOTc0NzQsInN1YiI6InNzdHJlY2tlIiwic2Vzc2lvbl9pZCI6IjY4ckFTODhwNm9yMzQzNEFoRlZ0c2c9PX5PS0lCM3d1SmdzQUlrV3lsTVdHMFZnYlQ1UVZWc3J4MDF3NjVIUmVHdmdBQ2wyenptcFBnZFhPTnc2OHhOU3dDdVNVMncyZWw0TWV3VWlvTjNpV1lWZ1EzSG5Rb2dzcHR0Y1hMaFRja21nOWQ0bHloK2ZhZmJlejFlZlM0T2JmbSIsImRvbWFpbiI6ImRlZmF1bHQifQ.m9mb4fcchnVZZn-2SxHLIGwqOl0fUwdJ6BskS5jALPF-cr_GKjo5Rm8jOygcih8-9gljOP5tWgfVCmzEFi7tftouEeWsKOfWB0xLB4ApXytMXVE3dGSFuSV92uLT-QTidojuIwwSJAc_95ZqWdefaZ6SNJy_KR6OAoonLfbjFvHYfa87EqX0OqrBG_ne2dtUAiUd801s3wW5EGFdBKVPQwai_u--4Z51mQL_cJY60qD4ipUIsry9re_YM-xvO4GDW7Ky8bQPitsuShlyfoXkUHxv3tSkrQt19kC2RyyX6DJNV-E9vBaQDADZc4Tc9Uaz9P2q-FvQ4-7xksjpBf97KA";
//  private static String token = "eyJraWQiOiJkZWZhdWx0IiwieDV0IjoiME4zaHdXVXlUZEVkdHpuSHctemY2RzQzX0tVIiwiYWxnIjoiUlMyNTYifQ.eyJleHAiOjE1NDA1NDgyOTksImp0aSI6Ikg2UFA2UGM4QmZRYi04S0IxMlRaQkEiLCJpYXQiOjE1NDA1NDQ2OTksInN1YiI6InNzdHJlY2tlIiwic2Vzc2lvbl9pZCI6IjRUZWk0MUNIQUJKN0Q0aUgra3BrTlE9PX5LU1l5UG1RTmNheGZ0SFlCQXVsdlVNVWNPMDlHRlNoOEJ6KzJQb0Vwc2Y2T2M1SndqNXo5U0VhOHV1S0djdmVIM09ibVdaTmdCV2IvcmxPalNuUUUyV3RWak5OQzJ1SWFEaWFYTXkxYWlzRWYxWCs5V1RpSThwSTJTZ1lZQVpySSIsImRvbWFpbiI6ImRlZmF1bHQifQ.Av6o60BoAZze26ps6ieE1jKbQHDLQ0TA8hKnM3bwwSVVjkgvCsZ8gxh6Wf7bH0oVRauyNJMS7Wq3wO2sYCZTEe4UsEjIySuYSSpkUf-gZCoenzsscjxagQ9xXeI3qEc9vrT9ovyjSG3OuTC3hkW6xvtdfsBUCt5auGM7xGmQBl-B36FzzwL4K699AwIeFGuLuxYN8nEhdVKOc0Ukw7YMLJoVJ2hUxxNTpQzU9SUu5rInuBc4Faia6UlXLQw5Amr4MGH325zIowxfSUzyp0voGetul5ZLLDLQi8TGNFiLT7uhDu28_ZHThhOkMjHZogLLQ6LCqEYOnsdLiu8fg-Abrw";
  private static String token = "eyJraWQiOiJkZWZhdWx0IiwieDV0IjoiME4zaHdXVXlUZEVkdHpuSHctemY2RzQzX0tVIiwiYWxnIjoiUlMyNTYifQ.eyJleHAiOjE1NDA1NDk5MjQsImp0aSI6IndzS0k5Z3NpREZqd0ZKMFgyc2F0TUEiLCJpYXQiOjE1NDA1NDYzMjQsInN1YiI6InNzdHJlY2tlIiwic2Vzc2lvbl9pZCI6IkZ2a2NRNXhQNUhRemNJWDNQTU5RdWc9PX5IOGJVMEZ3NXdFdHM0UDJ2Unl3dGlpb3Njaml6TElaSzYzRGVFZnhDLzhCWUV4aWxrcFEzUmtrbGFFdGF0YmFYZjhuQ2dTc2pXZUlVVnhqRHpEU3NUSDVHRi9RcENyelVub3U3MFQ3eWVpWDg5Y3REcjNQNjl6bUR5eTQyQ1BTUyIsImRvbWFpbiI6ImRlZmF1bHQifQ.Fhzh5V4faGAPyhWvqg10WgC3Yv1B5ZZy4vzOjw_TlKostx4XKkw7YXPMlqlVz5Wh68aFFvdQDFfHEhgHhWkZLesUJRRMpNzvQJAq5aXtondvMNRHH-0pNMpxSkt0GEiAfBp5ycmSTWg9B6ASvQy1xOh6DT4NsISjQHy2xq4Q-bQABfRI6K6DOlNpPu-C_scHWtypXe2np78_XqniHDQGnhwg-ynNVVlbup3pXITye8DcFAJN48r9vV6iWeSkQDb-V0QHvLon6TRRsFgj7HhyJW9CRcKndAFotiEn_6E8Qi7gaQgcwzywA4C47aYyS520Lcf9mN0rK9T2B55BNz6rGw";

  public static void main(String[] args) {
    final Decoder decoder     = Decoder.instance();
    final File    file        = new File("D:/Project/OracleConsultingServices12c/AccessManager/assertion-public.cer");
    final String  certificate = PEMUtility.certificateString(file);

    final Map<String, Verifier> verifier = new HashMap<String, Verifier>();
    verifier.put("RS256", Verifier.create(Algorithm.RS256, certificate));
    verifier.put("RS384", Verifier.create(Algorithm.RS384, certificate));
    verifier.put("RS512", Verifier.create(Algorithm.RS512, certificate));
    verifier.put("HS256", Verifier.create(Algorithm.HS256, "ifar42e5ntueh3f1t"));
    verifier.put("HS384", Verifier.create(Algorithm.HS384, "ifar42e5ntueh3f1t"));
    verifier.put("HS512", Verifier.create(Algorithm.HS512, "ifar42e5ntueh3f1t"));
    verifier.put("default", verifier.get("RS256"));
    try {
      decoder.decode(token, verifier);
    }
    catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}