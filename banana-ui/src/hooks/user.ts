import { useRouter } from 'vue-router';
import { Message } from '@arco-design/web-vue';

import {useLoginStore, useUserStore} from '@/store';

export default function useUser() {
  const router = useRouter();
  const userStore = useUserStore();
  const loginStore = useLoginStore();
  const logout = async (logoutTo?: string) => {
    await loginStore.logout();
    const currentRoute = router.currentRoute.value;
    Message.success('η»εΊζε');
    router.push({
      name: logoutTo && typeof logoutTo === 'string' ? logoutTo : 'login',
      query: {
        ...router.currentRoute.value.query,
        redirect: currentRoute.name as string,
      },
    });
  };
  return {
    logout,
  };
}
