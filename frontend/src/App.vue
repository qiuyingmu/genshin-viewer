<template>
  <div id="three-container"></div>
  <router-view />
</template>

<script setup>
import { onMounted, onBeforeUnmount } from 'vue';
import * as THREE from 'three';

let renderer, scene, camera, particles;
let mouseX = 0, mouseY = 0;
let animId = null;

const PARTICLE_COUNT = 250;

onMounted(() => {
  const container = document.getElementById('three-container');
  if (!container) return;

  scene = new THREE.Scene();
  camera = new THREE.PerspectiveCamera(60, container.clientWidth / container.clientHeight, 0.1, 1000);
  camera.position.z = 25;

  renderer = new THREE.WebGLRenderer({ alpha: true, antialias: true });
  renderer.setSize(container.clientWidth, container.clientHeight);
  renderer.setPixelRatio(Math.min(window.devicePixelRatio, 1.5));
  container.appendChild(renderer.domElement);

  // Particles
  const geo = new THREE.BufferGeometry();
  const pos = new Float32Array(PARTICLE_COUNT * 3);
  for (let i = 0; i < PARTICLE_COUNT; i++) {
    pos[i * 3] = (Math.random() - 0.5) * 70;
    pos[i * 3 + 1] = (Math.random() - 0.5) * 50;
    pos[i * 3 + 2] = (Math.random() - 0.5) * 30 - 5;
  }
  geo.setAttribute('position', new THREE.BufferAttribute(pos, 3));

  const mat = new THREE.PointsMaterial({
    color: '#7c3aed',
    size: 1.2, transparent: true, opacity: 0.2,
    blending: THREE.AdditiveBlending, depthWrite: false,
    sizeAttenuation: true,
  });
  particles = new THREE.Points(geo, mat);
  scene.add(particles);

  document.addEventListener('mousemove', onMouseMove);
  window.addEventListener('resize', onResize);

  // Theme observer
  const obs = new MutationObserver(() => {
    const isDark = document.documentElement.getAttribute('data-theme') === 'dark';
    if (particles) particles.material.opacity = isDark ? 0.4 : 0.2;
  });
  obs.observe(document.documentElement, { attributes: true, attributeFilter: ['data-theme'] });

  animate();
});

onBeforeUnmount(() => {
  if (animId) cancelAnimationFrame(animId);
  if (renderer) { renderer.dispose(); renderer.forceContextLoss(); }
});

function onMouseMove(e) {
  mouseX = (e.clientX / window.innerWidth) * 2 - 1;
  mouseY = -(e.clientY / window.innerHeight) * 2 + 1;
}

function onResize() {
  const c = document.getElementById('three-container');
  if (!c || !renderer) return;
  camera.aspect = c.clientWidth / c.clientHeight;
  camera.updateProjectionMatrix();
  renderer.setSize(c.clientWidth, c.clientHeight);
}

function animate() {
  animId = requestAnimationFrame(animate);
  if (particles) {
    particles.rotation.y += 0.0006;
    particles.rotation.x += (mouseY * 0.008 - particles.rotation.x) * 0.008;
    particles.position.y = Math.sin(Date.now() * 0.0003) * 0.3;
  }
  renderer.render(scene, camera);
}
</script>
