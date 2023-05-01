function isCrawler() {
  const crawlerKeywords = [
    'bot',
    'google',
    'bing',
    'yahoo',
    'spider',
    'crawl',
    'archiver',
    'curl',
    'python',
    'nutch',
    'harvest',
    'slurp',
    'scrapy',
    'crawl',
    'facebook',
    'twitter',
    'baidu',
    'yandex',
    'duckduckbot',
    'exabot',
    'sogou',
    'seznam',
    'alexa',
    'mj12bot',
    'semrushbot',
    'ahrefsbot',
    'lighthouse',
  ];

  const userAgent = navigator.userAgent.toLowerCase();
  // console.log('User agent: ' + userAgent);
  for (let i = 0; i < crawlerKeywords.length; i++) {
    if (userAgent.includes(crawlerKeywords[i])) {
      return true;
    }
  }

  return false;
}

export default isCrawler;
