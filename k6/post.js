import http from 'k6/http';
import { check } from 'k6';
import { uuidv4 } from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

export const options = {
    stages: [
        { duration: '10s', target: 10 },
        { duration: '10s', target: 20 },
        { duration: '30s', target: 0 },
    ],
    // thresholds: { http_req_duration: ['avg<100', 'p(95)<200'] },
    userAgent: 'MyK6UserAgentString/1.0',
};

export default function () {

    const randomUUID = uuidv4();

    const payload = JSON.stringify({
        correlationId: '' + randomUUID + '',
        amount: 41.9,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post('http://localhost:8080/payments', payload, params);

    check(res, {
        'status is 200': (r) => r.status === 200
    });
}