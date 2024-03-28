// https://k6.io/docs/get-started/installation/
import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
    thresholds: {
        http_req_failed: ['rate<0.01'], // http errors should be less than 1%
        http_req_duration: ['p(95)<200'], // 95% of requests should be below 200ms
    },
};

export default function () {
    sleep(1 + (Math.random() * 2));
    // http.get('http://localhost:8080/api/leaderboards/official/overall-ranking?page=1&limit=500');

    // ssh -N -L 8081:localhost:8080 root@drl-leaderboards-test.miau.io
    http.get('http://localhost:8081/api/leaderboards/official/overall-ranking?page=1&limit=500');
    // http.get('https://drl-leaderboards-test.miau.io/api/leaderboards/official/overall-ranking?page=1&limit=500');

 }

// Run with:
// k6 run --vus 500 --duration 10s --out json=test.json loadtest.k6.js
