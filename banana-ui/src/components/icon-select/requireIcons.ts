const modules = import.meta.glob('./../../assets/icons/svg/*.svg');
const icons = [] as Array<string>;

// eslint-disable-next-line guard-for-in,no-restricted-syntax
for (const path in modules) {
  const p = path.split('assets/icons/svg/')[1].split('.svg')[0];
  icons.push(p);
}

export default icons;
