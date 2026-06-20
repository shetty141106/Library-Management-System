import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      "/api": {
        target: "https://library-management-system-0g2d.onrender.com",
        changeOrigin: true
      }
    }
  }
});
