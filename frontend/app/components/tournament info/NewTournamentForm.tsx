"use client";

import { Button, DatePicker, Form, Input, Select } from "antd";
import Alert from "antd/es/alert/Alert";
import axios, { AxiosError } from "axios";
import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import { useState } from "react";
import dayjs from 'dayjs';
import { TournamentStatus } from "@/types/enums";

const { Option } = Select;

interface TournamentFormValues {
  name: string;
  type: string;
  addon: string;
  date: dayjs.Dayjs;
}

export default function NewTournamentForm() {
  const { data: session } = useSession();
  const router = useRouter();
  const [form] = Form.useForm();

  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const currentDate = new Date().toISOString().slice(0, 10);

  const handleSubmit = async (values: TournamentFormValues) => {
    if (!session?.accessToken) {
      setError("Brak autoryzacji. Zaloguj się ponownie.");
      return;
    }

    const payload = {
      name: values.name,
      type: values.type,
      addon: values.addon,
      date: values.date.format('YYYY-MM-DD'),
    };

    setSubmitting(true);
    setError(null);

    try {
      const response = await axios.post(
        "http://localhost:8080/tournaments",
        payload,
        { headers: { 
          Authorization: `Bearer ${session.accessToken}`,
          "Content-Type": "application/json" } 
        }
      );
      router.push(`/tournaments/${response.data.id}`);
    } catch (err: unknown) {
      if (err instanceof AxiosError) {
        setError(err.response?.data?.message || "Wystąpił błąd podczas tworzenia turnieju.");
      } else {
        setError("Wystąpił nieoczekiwany błąd.");
      }
      console.error(err);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Form
      form={form}
      layout="vertical"
      onFinish={handleSubmit}
      className="bg-[#1f2425] text-white p-6 rounded-2xl flex flex-col gap-4 w-full max-w-md"
      initialValues={{ type: "draft", date: dayjs() }}
    >
      <h2 className="text-2xl font-bold mb-6 text-center text-white">Stwórz nowy turniej</h2>

      {error && <Alert message={error} type="error" closable className="mb-4" />}

      <Form.Item
        name="name"
        label={<label style={{ color: 'white' }}>Nazwa turnieju:</label>}
        rules={[{ required: true, message: 'Nazwa turnieju jest wymagana!' }]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        name="type"
        label={<label style={{ color: 'white' }}>Typ rozgrywki:</label>}
      >
        <Select>
          <Option value="draft">Draft</Option>
          <Option value="sealed">Sealed</Option>
          <Option value="commander">Commander</Option>
          <Option value="modern">Modern</Option>
        </Select>
      </Form.Item>

      <Form.Item
        name="addon"
        label={<label style={{ color: 'white' }}>Dodatek:</label>}
      >
        <Input />
      </Form.Item>

      <Form.Item
        name="date"
        label={<label style={{ color: 'white' }}>Data:</label>}
      >
        <DatePicker style={{ width: '100%' }} />
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit" loading={submitting} block>
          Stwórz turniej
        </Button>
      </Form.Item>
    </Form>
  );
}
