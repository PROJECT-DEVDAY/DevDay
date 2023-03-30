export default function handler(req, res) {
  const { challengeId } = req.query;
  res.status(200).json({
    status: 200,
    message: 'success',
    data: {
      userId: 1,
      challengeId: challengeId,
      approve: true,
    },
  });
}
